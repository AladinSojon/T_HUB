package net.therap.mealsystem.repository;

import net.therap.mealsystem.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sheikh.ishrak
 * @since 20/03/2022
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);
}
