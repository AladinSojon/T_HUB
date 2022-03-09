package net.therap.mealsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author aladin
 * @since 2/28/22
 */

@Data
@AllArgsConstructor
@Document(collection = "user")
public class User {

    private String id;

    @NotNull(message = "User name cannot be null")
    private String name;
    private List<String> notifList;
    private Date created;
    private Date updated;
}
