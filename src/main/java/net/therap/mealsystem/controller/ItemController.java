package net.therap.mealsystem.controller;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author aladin
 * @since 3/1/22
 */
@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<?> show(@RequestParam String id) {
        try {
            return new ResponseEntity<>(itemService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/item")
    public ResponseEntity<?> save(@RequestBody Item item) {
        try {
            itemService.save(item);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/item/list")
    public ResponseEntity<?> showItems() {
        List<Item> itemList = itemService.findAll();

        return new ResponseEntity<>(itemList, itemList.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/item")
    public ResponseEntity<?> update(@RequestParam String id, @RequestBody Item item) {
        try {
            itemService.updateItem(id, item);
            return new ResponseEntity<>("Update Item with id " + id, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> delete(@RequestParam String id) {
        try {
            itemService.deleteItemById(id);
            return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
