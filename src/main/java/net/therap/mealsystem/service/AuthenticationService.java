package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.PasswordResetToken;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.domain.UserAccountStatus;
import net.therap.mealsystem.exception.CollectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

/**
 * @author sheikh.ishrak
 * @since 20/03/2022
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendPasswordResetToken(String usernameOrEmail) {
        User user = usernameOrEmail.contains("@") ? userService.findByEmail(usernameOrEmail) :
                userService.findByUsername(usernameOrEmail);

        if (ObjectUtils.isEmpty(user)) {
            throw new IllegalStateException("No user found for the provided username/email");
        }

        if (UserAccountStatus.DELETED.name().equals(user.getAccountStatus())) {
            throw new IllegalStateException("Account no longer exists");
        }

        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setUser(user);

        passwordResetTokenService.save(passwordResetToken);

        mailService.sendPasswordResetMail(user, passwordResetToken);
    }

    public void setNewPassword(String token, String password) throws CollectionException {
        passwordResetTokenService.validateToken(token);

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
        User user = passwordResetToken.getUser();

        user.setPassword(passwordEncoder.encode(password));
        userService.update(user.getId(), user);

        //passwordResetTokenService.delete(passwordResetToken.getId());
    }
}
