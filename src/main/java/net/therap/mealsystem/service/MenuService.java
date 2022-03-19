package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.domain.Menu;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.dto.MealDto;
import net.therap.mealsystem.dto.MenuDto;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
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
    private ItemService itemService;

    @Autowired
    private UserService userService;

    public void save(Menu menu) throws ConstraintViolationException {
        List<Item> itemList = new ArrayList<>();

        for (Item item : menu.getItemList()) {
            Optional<Item> itemDb = itemService.findById(item.getId());

            itemDb.ifPresent(itemList::add);
        }

        Optional<User> createdBy = userService.findById(menu.getCreatedBy().getId());
        createdBy.ifPresent(menu::setCreatedBy);

        menu.setItemList(itemList);
        menu.setCreated(new Date((System.currentTimeMillis())));
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
            menuDto.setDay(mealDate.getDayOfWeek().name());

            for (Menu menu : menusForDate) {
                MealDto mealDto = new MealDto();
                mealDto.setMealTime(menu.getMealTime());
                mealDto.setItemMap(menu.getItemList().stream().collect(Collectors.toMap(Item::getId, Item::getName)));
                mealDto.setHeadCount(menu.getHeadCount());

                menuDto.getMealList().add(mealDto);
            }

            menuDtos.add(menuDto);
        }

        return menuDtos;
    }
}
