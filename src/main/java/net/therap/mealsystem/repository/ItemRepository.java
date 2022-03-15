package net.therap.mealsystem.repository;

import net.therap.mealsystem.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author aladin
 * @since 3/9/22
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Item findByName(String name);
}