package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.AccountConfirmationToken;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.domain.UserAccountStatus;
import net.therap.mealsystem.dto.RegistrationRequest;
import net.therap.mealsystem.exception.CollectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

import static net.therap.mealsystem.domain.UserRole.BASIC_USER;
import static net.therap.mealsystem.domain.UserRole.MEAL_USER;
import static net.therap.mealsystem.util.Constants.EMAIL_PATTERN;
import static net.therap.mealsystem.util.Constants.THERAP_EMAIL_PATTERN;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
@Service
public class RegistrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountConfirmationTokenService accountConfirmationTokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void validateRegistration(RegistrationRequest registrationRequest) throws CollectionException {
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        Pattern therapEmailPattern = Pattern.compile(THERAP_EMAIL_PATTERN);

        if (!emailPattern.matcher(registrationRequest.getEmail()).matches()) {
            throw new IllegalStateException("Not a valid Email address");
        }

        if (!therapEmailPattern.matcher(registrationRequest.getEmail()).matches()) {
            //throw new IllegalStateException("Not a valid Therap Email address");
        }

        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new IllegalStateException("Entered passwords aren't same");
        }

        if (!ObjectUtils.isEmpty(userService.findByUsername(registrationRequest.getUsername()))) {
            throw new CollectionException((CollectionException.alreadyExists()));
        }

        if (!ObjectUtils.isEmpty(userService.findByEmail(registrationRequest.getEmail()))) {
            throw new IllegalStateException("User with same email exists, please choose a different email address");
        }
    }

    public void register(RegistrationRequest registrationRequest) throws CollectionException {
        User user = new User();

        user.setUsername(registrationRequest.getUsername());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRoles(Arrays.asList(MEAL_USER, BASIC_USER));
        user.setAccountStatus(UserAccountStatus.NOT_CONFIRMED);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);

        userService.save(user);

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken();
        String token = UUID.randomUUID().toString();

        accountConfirmationToken.setToken(token);
        accountConfirmationToken.setUser(user);

        accountConfirmationTokenService.save(accountConfirmationToken);

        mailService.sendConfirmationMail(user, accountConfirmationToken);
    }
}
