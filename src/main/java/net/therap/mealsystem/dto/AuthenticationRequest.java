package net.therap.mealsystem.dto;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
public class AuthenticationRequest {

    String usernameOrEmail;

    String password;

    public AuthenticationRequest() {
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
