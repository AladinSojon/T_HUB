package net.therap.mealsystem.repository;

import net.therap.mealsystem.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author aladin
 * @author sheikh.ishrak
 * @since 3/13/22
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findByMealDateGreaterThanEqualAndMealDateLessThanEqual(Date fromDate, Date toDate);

    List<Menu> findByMealDateEquals(Date date);
}
