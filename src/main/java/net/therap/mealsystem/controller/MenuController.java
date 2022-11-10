package net.therap.mealsystem.controller;

import net.therap.mealsystem.domain.Menu;
import net.therap.mealsystem.dto.MenuDto;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.service.MenuService;
import net.therap.mealsystem.util.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author aladin
 * @author sheikh.ishrak
 * @since 3/1/22
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menu/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN', 'MEAL_USER')")
    public ResponseEntity<?> show(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(menuService.getMenuById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/menu")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN')")
    public ResponseEntity<?> save(@RequestBody Menu menu) {
        try {
            menuService.save(menu);
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/menu/list")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN', 'MEAL_USER')")
    public ResponseEntity<?> showMenus() {
        List<MenuDto> menuList = menuService.getMenusForView();

        return new ResponseEntity<>(menuList, menuList.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/menu/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Menu menu) {
        try {
            menuService.update(id, menu);
            return new ResponseEntity<>("Update Menu with id " + id, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/menu/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            menuService.delete(id);
            return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/menu/mealPreference")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN', 'MEAL_USER')")
    public ResponseEntity<?> showMealPreference(@RequestParam("date") String date) {
        menuService.validateMealPreference(date);

        Date mealDate = Date.from(LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant());

        JSONObject jsonObject = menuService.getMealPreference(mealDate);

        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    @PostMapping("/menu/mealPreference")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN', 'MEAL_USER')")
    public ResponseEntity<?> setMealPreference(@RequestBody JSONObject jsonObject) throws ParseException, CollectionException {
        menuService.setMealPreference(jsonObject);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
