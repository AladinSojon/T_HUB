package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.domain.MealTime;
import net.therap.mealsystem.domain.Menu;
import net.therap.mealsystem.dto.MenuDto;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.repository.MenuRepository;
import net.therap.mealsystem.util.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author aladin
 * @author sheikh.ishrak
 * @since 3/6/22
 */
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserService userService;

    public void save(Menu menu) throws ConstraintViolationException {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        menu.setDay(simpleDateformat.format(menu.getMealDate()));
        menuRepository.save(menu);
    }

    public List<Menu> findAll() {
        List<Menu> menuList = menuRepository.findAll();

        if (menuList.size() > 0) {
            return menuList;
        } else {
            return new ArrayList<>();
        }
    }

    public Menu getMenuById(Integer id) throws CollectionException {
        Optional<Menu> menuDb = menuRepository.findById(id);

        if (!menuDb.isPresent()) {
            throw new CollectionException((CollectionException.notFoundException(id)));
        } else {
            return menuDb.get();
        }
    }

    public List<Menu> findByMealDate(Date date) {
        return menuRepository.findByMealDateEquals(date);
    }

    public void update(Integer id, Menu menu) throws CollectionException {
        Optional<Menu> menuDb = menuRepository.findById(id);
        if (menuDb.isPresent()) {
            Menu updatedMenu = menuDb.get();
            updatedMenu.setDay(menu.getDay());
            updatedMenu.setMealTime(menu.getMealTime());
            updatedMenu.setItemList(menu.getItemList());
            updatedMenu.setCreatedBy(menu.getCreatedBy());
            updatedMenu.setUpdated(new Date(System.currentTimeMillis()));
            menuRepository.save(updatedMenu);
        } else {
            throw new CollectionException(CollectionException.notFoundException(id));
        }
    }

    public void delete(Integer id) throws CollectionException {
        Optional<Menu> menu = menuRepository.findById(id);

        if (!menu.isPresent()) {
            throw new CollectionException(CollectionException.notFoundException(id));
        } else {
            menuRepository.delete(menu.get());
        }
    }

    public List<MenuDto> getMenusForView() {
        List<MenuDto> menuDtos = new ArrayList<>();

        LocalDate fromDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate toDate = fromDate.plusDays(10);

        List<Menu> menuList = menuRepository.findByMealDateGreaterThanEqualAndMealDateLessThanEqual(
                Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        for (LocalDate date = fromDate; date.isBefore(toDate) || date.equals(toDate); date = date.plusDays(1)) {
            LocalDate mealDate = date;
            List<Menu> menusForDate = menuList
                    .stream()
                    .filter(menu -> menu.getMealDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(mealDate))
                    .collect(Collectors.toList());

            if (menusForDate.isEmpty()) {
                continue;
            }

            MenuDto menuDto = new MenuDto();

            menuDto.setDate(mealDate);
            menuDto.setDay(StringUtils.capitalize(mealDate.getDayOfWeek().name().toLowerCase()));

            for (MealTime mealTime : MealTime.values()) {
                String itemListStr = "";
                int headCount = 0;

                Optional<Menu> menuForMealTime = menusForDate
                        .stream()
                        .filter(menu -> menu.getMealTime().equals(mealTime))
                        .findFirst();

                if (menuForMealTime.isPresent()) {
                    Menu menu = menuForMealTime.get();

                    itemListStr = menu.getItemList()
                            .stream()
                            .map(Item::getName)
                            .collect(Collectors.joining(", "));

                    headCount = menu.getHeadCount();
                }

                menuDto.getMealList().put("Items (" + StringUtils.capitalize(mealTime.name().toLowerCase()) + ")", itemListStr);
                menuDto.getMealList().put("Head Count (" + StringUtils.capitalize(mealTime.name().toLowerCase()) + ")", headCount);
            }

            menuDtos.add(menuDto);
        }

        return menuDtos;
    }

    public JSONObject getMealPreference(Date date) {
        JSONObject jsonObject = new JSONObject();

        String currentLoggedInUsername = userService.getCurrentLoggedInUsername();

        List<Menu> menuList = findByMealDate(date);

        //jsonObject.put("date", date);
        //jsonObject.put("menuList", menuList);

        for (Menu menu : menuList) {
            boolean isNotInterestedInThisMenu = menu.getNotInterestedUserList()
                    .stream()
                    .anyMatch(user -> currentLoggedInUsername.equals(user.getUsername()));

            jsonObject.put(menu.getMealTime().name(), !isNotInterestedInThisMenu);
        }

        return jsonObject;
    }

    public void setMealPreference(JSONObject jsonObject) throws ParseException, CollectionException {
        String currentLoggedInUsername = userService.getCurrentLoggedInUsername();

        Date mealDate = Date.from(LocalDate.parse((String) jsonObject.get("date")).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Menu> menuList = findByMealDate(mealDate);

        for (String key : jsonObject.keySet()) {
            if ("date".equals(key)) {
                continue;
            }

            Optional<Menu> optionalMenu = menuList.stream().filter(m -> m.getMealTime().equals(MealTime.valueOf(key))).findFirst();
            if (optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();

                boolean isExistingNotInterestedInThisMenu = menu.getNotInterestedUserList()
                        .stream()
                        .anyMatch(user -> currentLoggedInUsername.equals(user.getUsername()));

                boolean isCurrentlyNotInterestedInThisMenu = !((boolean) jsonObject.get(key));

                if (!isCurrentlyNotInterestedInThisMenu && isExistingNotInterestedInThisMenu) {
                    menu.getNotInterestedUserList().remove(userService.findByUsername(currentLoggedInUsername));
                    menu.setHeadCount(menu.getHeadCount() + 1);
                } else if (isCurrentlyNotInterestedInThisMenu && !isExistingNotInterestedInThisMenu) {
                    menu.getNotInterestedUserList().add(userService.findByUsername(currentLoggedInUsername));
                    menu.setHeadCount(menu.getHeadCount() - 1);
                }

                update(menu.getId(), menu);
            }
        }
    }

    public void validateMealPreference(String date) {
        Date now = new Date(System.currentTimeMillis());
        Date validUpto = Date.from(LocalDate.parse(date).minusDays(1).atTime(17, 0).atZone(ZoneId.systemDefault()).toInstant());

        if (now.after(validUpto)) {
            throw new IllegalStateException("You must submit meal preference at least a day before (within 5 PM)");
        }
    }
}
