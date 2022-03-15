package net.therap.mealsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aladin
 * @since 3/1/22
 */
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "menu")
public class Menu extends Persistent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Day day;

    @Column(name = "meal_time")
    private MealTime mealTime;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "menu_item", joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")})
    private List<Item> itemList;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "meal_date")
    private Date mealDate;

    @Column(name = "head_count")
    private int headCount;

    @ManyToOne
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    public Menu() {
        itemList = new ArrayList<>();
    }
}
