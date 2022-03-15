package net.therap.mealsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aladin
 * @since 2/28/22
 */
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends Persistent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "User name cannot be null")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "user_notif_list", joinColumns = {@JoinColumn(name = "user_id")})
    @Column(name = "notif")
    @OrderColumn(name = "idx")
    private List<String> notifList;

    public User() {
        notifList = new ArrayList<>();
    }
}
