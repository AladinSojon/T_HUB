package net.therap.mealsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author aladin
 * @since 3/9/22
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUid = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
