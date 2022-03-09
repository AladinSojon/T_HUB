package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.domain.Menu;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.exception.CollectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aladin
 * @since 3/6/22
 */
@Service
public class MenuService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    public Menu findById(String id) {
        return mongoTemplate.findById(id, Menu.class);
    }

    public void save(Menu menu) throws ConstraintViolationException {
        List<Item> itemList = new ArrayList<>();

        for (Item item: menu.getItemList()) {
            Item itemDb = itemService.findById(item.getId());

            if (!ObjectUtils.isEmpty(itemDb)) {
                itemList.add(itemDb);
            }
        }

        User createdBy = userService.findById(menu.getCreatedBy().getId());
        if (!ObjectUtils.isEmpty(createdBy)) {
            menu.setCreatedBy(createdBy);
        }

        menu.setItemList(itemList);
        menu.setCreated(new Date((System.currentTimeMillis())));
        mongoTemplate.save(menu);
    }

    public List<Menu> findAll() {
        List<Menu> menuList = mongoTemplate.findAll(Menu.class);

        if (menuList.size() > 0) {
            return menuList;
        } else {
            return new ArrayList<>();
        }
    }

    public Menu getMenuById(String id) throws CollectionException {
        Menu menuDb = findById(id);

        if (ObjectUtils.isEmpty(menuDb)) {
            throw new CollectionException((CollectionException.notFoundException(id)));
        } else {
            return menuDb;
        }
    }

    public void update(String id, Menu menu) throws CollectionException {
        Menu menuDb = findById(id);
        if (!ObjectUtils.isEmpty(menuDb)) {
            menuDb.setDay(menu.getDay());
            menuDb.setMealTime(menu.getMealTime());
            menuDb.setItemList(menu.getItemList());
            menuDb.setUpdated(new Date(System.currentTimeMillis()));
            mongoTemplate.save(menuDb);
        } else {
            throw new CollectionException(CollectionException.notFoundException(id));
        }
    }

    public void delete(String id) throws CollectionException {
        Menu menu = findById(id);
        if (ObjectUtils.isEmpty(menu)) {
            throw new CollectionException(CollectionException.notFoundException(id));
        } else {
            mongoTemplate.remove(menu);
        }
    }
}
