package net.therap.mealsystem.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import net.therap.mealsystem.domain.Day;
import net.therap.mealsystem.domain.MealTime;
import net.therap.mealsystem.util.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author aladin
 * @since 3/19/22
 */

@RestController
public class MiscController {

    @GetMapping("/mealTime/list")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MEAL_ADMIN')")
    public List<JSONObject> showMealTimes() {
        List<JSONObject> jsonList = new ArrayList<>();
        for (MealTime mealTime: MealTime.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", mealTime.name());
            jsonObject.put("name", mealTime.getLabel());

            jsonList.add(jsonObject);
        }

        return jsonList;
    }
}
