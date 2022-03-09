package net.therap.mealsystem.controller;

import net.therap.mealsystem.domain.Menu;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.service.MenuService;
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
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menu")
    public ResponseEntity<?> show(@RequestParam String id) {
        try {
            return new ResponseEntity<>(menuService.getMenuById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/menu")
    public ResponseEntity<?> save(@RequestBody Menu menu) {
        try {
            menuService.save(menu);
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/menu/list")
    public ResponseEntity<?> showMenus() {
        List<Menu> menuList = menuService.findAll();

        return new ResponseEntity<>(menuList, menuList.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/menu")
    public ResponseEntity<?> update(@RequestParam String id, @RequestBody Menu menu) {
        try {
            menuService.update(id, menu);
            return new ResponseEntity<>("Update Item with id " + id, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/menu")
    public ResponseEntity<?> delete(@RequestParam String id) {
        try {
            menuService.delete(id);
            return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
