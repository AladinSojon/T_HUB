package net.therap.mealsystem.controller;

import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.service.AuthenticationService;
import net.therap.mealsystem.service.PasswordResetTokenService;
import net.therap.mealsystem.util.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author sheikh.ishrak
 * @since 09/03/2022
 */
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/resetPassword")
    public ResponseEntity<?> sendPasswordResetToken(@RequestBody JSONObject jsonObject) {
        String usernameOrEmail = (String) jsonObject.get("usernameOrEmail");
        authenticationService.sendPasswordResetToken(usernameOrEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token) {
        passwordResetTokenService.validateToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resetPassword/setNewPassword")
    public ResponseEntity<?> setNewPassword(@RequestBody JSONObject jsonObject) throws CollectionException {
        String token = (String) jsonObject.get("token");
        String password = (String) jsonObject.get("password");
        String confirmPassword = (String) jsonObject.get("confirmPassword");

        authenticationService.setNewPassword(token, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/accountUnverified")
    public String showAccessDenied() {
        return "Account not confirmed yet.";
    }
}
