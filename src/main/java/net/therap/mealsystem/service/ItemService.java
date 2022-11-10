package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author aladin
 * @since 3/1/22
 */
@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Optional<Item> findById(int id) {
        return itemRepository.findById(id);
    }

    public Item findByName(String name) {
        return itemRepository.findByName(name);
    }

    public void save(Item item) throws ConstraintViolationException, CollectionException {
        Item itemDb = findByName(item.getName());

        if (!ObjectUtils.isEmpty(itemDb)) {
            throw new CollectionException((CollectionException.alreadyExists()));
        } else {
            item.setCreated(new Date((System.currentTimeMillis())));
            itemRepository.save(item);
        }
    }

    public List<Item> findAll() {
        List<Item> itemList = itemRepository.findAll();

        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }

    public void updateItem(int id, Item item) throws CollectionException {
        Optional<Item> itemDB = findById(id);
        Item itemWithSameName = findByName(item.getName());

        if (itemDB.isPresent()) {
            if (!ObjectUtils.isEmpty(itemWithSameName) && itemWithSameName.getId() != id) {
                throw new CollectionException(CollectionException.alreadyExists());
            }

            Item updatedItem = itemDB.get();

            updatedItem.setName(item.getName());
            updatedItem.setDescription(item.getDescription());
            updatedItem.setUpdated(new Date(System.currentTimeMillis()));
            itemRepository.save(updatedItem);
        } else {
            throw new CollectionException(CollectionException.notFoundException(id));
        }
    }

    public void deleteItemById(Integer id) throws CollectionException {
        Optional<Item> item = findById(id);
        if (!item.isPresent()) {
            throw new CollectionException(CollectionException.notFoundException(id));
        } else {
            itemRepository.delete(item.get());
        }
    }
}
