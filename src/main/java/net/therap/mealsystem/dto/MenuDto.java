package net.therap.mealsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author sheikh.ishrak
 * @since 16/03/2022
 */
@Getter
@Setter
@AllArgsConstructor
public class MenuDto {

    private LocalDate date;
    private String day;

    MealDto mealList;

    public MenuDto() {
        mealList = new MealDto();
    }
}
