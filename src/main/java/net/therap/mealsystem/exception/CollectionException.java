package net.therap.mealsystem.exception;

/**
 * @author aladin
 * @since 3/1/22
 */
public class CollectionException extends Exception {

    public static final long serialVersionUID = 1L;

    public CollectionException(String message) {
        super(message);
    }

    public static String notFoundException(String id) {
        return "Not found";
    }

    public static String alreadyExists() {
        return "Already exists with the name";
    }
}
