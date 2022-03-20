package net.therap.mealsystem.controller;

import net.therap.mealsystem.dto.RegistrationRequest;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.service.AccountConfirmationTokenService;
import net.therap.mealsystem.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

/**
 * @author sheikh.ishrak
 * @since 09/03/2022
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AccountConfirmationTokenService accountConfirmationTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            registrationService.validateRegistration(registrationRequest);
            registrationService.register(registrationRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/signup/confirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) throws CollectionException {
        accountConfirmationTokenService.confirm(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
