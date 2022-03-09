package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.Item;
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
 * @since 3/1/22
 */
@Service
public class ItemService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Item findById(String id) {
        return mongoTemplate.findById(id, Item.class);
    }

    public Item findByName(String name) {
        return mongoTemplate.findById(name, Item.class);
    }

    public void save(Item item) throws ConstraintViolationException, CollectionException {
        Item itemDb = findByName(item.getName());

        if (!ObjectUtils.isEmpty(itemDb)) {
            throw new CollectionException((CollectionException.alreadyExists()));
        } else {
            item.setCreated(new Date((System.currentTimeMillis())));
            mongoTemplate.save(item);
        }
    }

    public List<Item> findAll() {
        List<Item> itemList = mongoTemplate.findAll(Item.class);

        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }

    public void updateItem(String id, Item item) throws CollectionException {
        Item itemDB = findById(id);
        Item itemWithSameName = findByName(item.getName());

        if (!ObjectUtils.isEmpty(itemDB)) {
            if (!ObjectUtils.isEmpty(itemWithSameName) && !itemWithSameName.getId().equals(id)) {
                throw new CollectionException(CollectionException.alreadyExists());
            }

            itemDB.setName(item.getName());
            itemDB.setUpdated(new Date(System.currentTimeMillis()));
            mongoTemplate.save(itemDB);
        } else {
            throw new CollectionException(CollectionException.notFoundException(id));
        }
    }

    public void deleteItemById(String id) throws CollectionException {
        Item item = findById(id);
        if (ObjectUtils.isEmpty(item)) {
            throw new CollectionException(CollectionException.notFoundException(id));
        } else {
            mongoTemplate.remove(item);
        }
    }
}
