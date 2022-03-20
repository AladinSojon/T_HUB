package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.AccountConfirmationToken;
import net.therap.mealsystem.domain.PasswordResetToken;
import net.therap.mealsystem.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendConfirmationMail(User user, AccountConfirmationToken accountConfirmationToken) {

        String confirmationLink = "http://localhost:8080/signup/confirm?token=" + accountConfirmationToken.getToken();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());

        simpleMailMessage.setSubject("Confirm your MealSystem Account");
        simpleMailMessage.setText("Hi " + user.getUsername() + ", " +
                "\n\nWelcome to MealSystem!" +
                "\nPlease confirm your account using the following link: " +
                "\n\n" + confirmationLink);

        javaMailSender.send(simpleMailMessage);
    }

    public void sendPasswordResetMail(User user, PasswordResetToken passwordResetToken) {

        String passwordResetLink = "http://localhost:8080/resetPassword?token=" + passwordResetToken.getToken();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());

        simpleMailMessage.setSubject("Reset your Password");
        simpleMailMessage.setText("Hi " + user.getUsername() + ", " +
                "\n\nA request to reset your password had been issued." +
                "\nYou can change your password using the following link: " +
                "\n\n" + passwordResetLink +
                "\n\nThe link will expire in 3 hours.");

        javaMailSender.send(simpleMailMessage);
    }
}
