package net.therap.mealsystem.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.therap.mealsystem.util.JSONObject;

import java.time.LocalDate;

/**
 * @author sheikh.ishrak
 * @since 16/03/2022
 */
@Getter
@Setter
@NoArgsConstructor
public class MenuDto extends JSONObject {

    private LocalDate date;
    private String day;

    public MenuDto(LocalDate date, String day) {
        this.date = date;
        this.day = day;

        this.put("date", date);
        this.put("day", day);
    }
}
