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
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/item/list")
    public List<Item> showItems() {
        return itemService.findAll();
    }

    @PostMapping("/item")
    public void save(@RequestBody Item item) throws CollectionException {
        itemService.save(item);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> showItemById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(itemService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Item item) {
        try {
            itemService.updateItem(id, item);
            return new ResponseEntity<>("Update Item with id " + id, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            itemService.deleteItemById(id);
            return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
