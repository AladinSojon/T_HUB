package net.therap.mealsystem.util;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
public interface Constants {

    String[] PERMIT_ALL_PATTERNS = {"/login", "/logout", "/signup/**", "/resendConfirmation", "/resetPassword/**", "/accessDenied", "/accountUnverified"};
    String THERAP_EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@therapservices.net$";
    String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
}
