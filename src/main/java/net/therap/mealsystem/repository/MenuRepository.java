package net.therap.mealsystem.repository;

import net.therap.mealsystem.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author aladin
 * @since 3/13/22
 */
@Repository
public interface MenuRepository  extends JpaRepository<Menu, Integer> {
}
