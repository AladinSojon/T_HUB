package net.therap.mealsystem.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author aladin
 * @since 3/1/22
 */
@Data
@AllArgsConstructor
@Document(collection = "item")
public class Item {

    @Id
    private String id;

    @NotNull(message = "Item name cannot be null")
    private String name;
    private Date created;
    private Date updated;
}
