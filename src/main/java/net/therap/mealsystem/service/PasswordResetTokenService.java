package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.PasswordResetToken;
import net.therap.mealsystem.domain.UserAccountStatus;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;

/**
 * @author sheikh.ishrak
 * @since 20/03/2022
 */
@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    UserService userService;

    public void validateToken(String token) {
        PasswordResetToken passwordResetToken = findByToken(token);

        if (passwordResetToken.getExpiration().before(new Date(System.currentTimeMillis()))) {
            throw new IllegalStateException("Password Reset Token expired");
        }

        if (UserAccountStatus.DELETED.equals(passwordResetToken.getUser().getAccountStatus())) {
            throw new IllegalStateException("Account no longer exists");
        }
    }

    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public void save(PasswordResetToken passwordResetToken) throws ConstraintViolationException {
        passwordResetToken.setCreated(new Date(System.currentTimeMillis()));
        passwordResetToken.setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000));
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public void delete(Integer id) throws CollectionException {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findById(id);

        if (!passwordResetToken.isPresent()) {
            throw new CollectionException(CollectionException.notFoundException(id));
        } else {
            passwordResetTokenRepository.delete(passwordResetToken.get());
        }
    }
}
