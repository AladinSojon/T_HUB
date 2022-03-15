package net.therap.mealsystem.repository;

import net.therap.mealsystem.domain.AccountConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sheikh.ishrak
 * @since 14/03/2022
 */
@Repository
public interface AccountConfirmationTokenRepository extends JpaRepository<AccountConfirmationToken, Integer> {

    AccountConfirmationToken findByToken(String token);
}
