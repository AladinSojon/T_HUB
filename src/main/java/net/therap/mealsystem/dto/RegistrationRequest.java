package net.therap.mealsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
@Data
@AllArgsConstructor
public class RegistrationRequest {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}
