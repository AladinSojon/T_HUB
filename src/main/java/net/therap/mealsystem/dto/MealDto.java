package net.therap.mealsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.therap.mealsystem.domain.MealTime;

import java.util.Map;

/**
 * @author sheikh.ishrak
 * @since 17/03/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDto {

    private MealTime mealTime;
    private Map<Integer, String> itemMap;
    private int headCount;
}
