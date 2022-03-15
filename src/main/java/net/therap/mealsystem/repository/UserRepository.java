package net.therap.mealsystem.repository;

import net.therap.mealsystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author aladin
 * @author sheikh.ishrak
 * @since 3/11/22
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);
}
