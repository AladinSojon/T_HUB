package net.therap.mealsystem.controller;

import net.therap.mealsystem.domain.MealTime;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.domain.UserRole;
import net.therap.mealsystem.service.MenuService;
import net.therap.mealsystem.service.UserService;
import net.therap.mealsystem.util.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author aladin
 * @since 3/19/22
 */

@RestController
public class MiscController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/mealTime/list")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN')")
    public List<JSONObject> showMealTimes() {
        List<JSONObject> jsonList = new ArrayList<>();
        for (MealTime mealTime : MealTime.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", mealTime.name());
            jsonObject.put("name", mealTime.getLabel());

            jsonList.add(jsonObject);
        }

        return jsonList;
    }

    @GetMapping("/role/list")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public List<JSONObject> showRoleList() {
        List<JSONObject> jsonList = new ArrayList<>();
        for (UserRole role : UserRole.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", role.name());
            jsonObject.put("name", role.name());

            jsonList.add(jsonObject);
        }

        return jsonList;
    }

    @GetMapping("/user/role/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public List<JSONObject> showUserRoleList(@PathVariable Integer id) {
        Optional<User> userOptional = userService.findById(id);
        List<JSONObject> jsonList = new ArrayList<>();

        if (userOptional.isPresent()) {
            for (UserRole role : userOptional.get().getRoles()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("key", role.name());
                jsonObject.put("name", role.name());

                jsonList.add(jsonObject);
            }
        }

        return jsonList;
    }

    @GetMapping("/accessDenied")
    public ResponseEntity<?> showAccessDenied() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("message", "Insufficient Privileges, Access Denied.");
        jsonObject.put("access", "accessDenied");

        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }
}
