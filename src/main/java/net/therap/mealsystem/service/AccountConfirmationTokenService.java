package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.AccountConfirmationToken;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.domain.UserAccountStatus;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.repository.AccountConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
@Service
public class AccountConfirmationTokenService {

    @Autowired
    private AccountConfirmationTokenRepository accountConfirmationTokenRepository;

    @Autowired
    UserService userService;

    public void confirm(String token) throws CollectionException {
        AccountConfirmationToken accountConfirmationToken = findByToken(token);

        validateToken(accountConfirmationToken);

        User user = accountConfirmationToken.getUser();
        user.setAccountStatus(UserAccountStatus.CONFIRMED);
        user.setEnabled(true);
        userService.update(user.getId(), user);
    }

    public void validateToken(AccountConfirmationToken accountConfirmationToken) {
        if (accountConfirmationToken.getExpiration().before(new Date(System.currentTimeMillis()))) {
            throw new IllegalStateException("Confirmation Token expired");
        }

        if (UserAccountStatus.DELETED.equals(accountConfirmationToken.getUser().getAccountStatus())) {
            throw new IllegalStateException("Account no longer exists");
        }

        if (UserAccountStatus.CONFIRMED.equals(accountConfirmationToken.getUser().getAccountStatus())) {
            throw new IllegalStateException("Account already confirmed");
        }
    }

    public AccountConfirmationToken findByToken(String token) {
        return accountConfirmationTokenRepository.findByToken(token);
    }

    public List<AccountConfirmationToken> findAll() {
        List<AccountConfirmationToken> confirmationTokens = accountConfirmationTokenRepository.findAll();

        if (confirmationTokens.size() > 0) {
            return confirmationTokens;
        } else {
            return new ArrayList<>();
        }
    }

    public void save(AccountConfirmationToken accountConfirmationToken) throws ConstraintViolationException {
        accountConfirmationToken.setCreated(new Date(System.currentTimeMillis()));
        accountConfirmationToken.setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        accountConfirmationTokenRepository.save(accountConfirmationToken);
    }
}
