package net.therap.mealsystem.repository;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author aladin
 * @since 3/11/22
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByName(String name);
}
