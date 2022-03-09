package net.therap.mealsystem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aladin
 * @since 3/1/22
 */
@Data
@AllArgsConstructor
@Document(collection = "menu")
public class Menu {

    @Id
    private String id;

    private Day day;
    private MealTime mealTime;

    @DBRef
    private List<Item> itemList;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    private int headCount;

    @DBRef
    private User createdBy;

    @DBRef
    private User updatedBy;

    private Date created;
    private Date updated;

    public Menu() {
        itemList = new ArrayList<>();
    }
}
