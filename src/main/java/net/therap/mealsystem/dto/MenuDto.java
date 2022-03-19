package net.therap.mealsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sheikh.ishrak
 * @since 16/03/2022
 */
@Data
@AllArgsConstructor
public class MenuDto {

    private LocalDate date;
    private String day;

    private List<MealDto> mealList;

    public MenuDto() {
        mealList = new ArrayList<>();
    }
}
