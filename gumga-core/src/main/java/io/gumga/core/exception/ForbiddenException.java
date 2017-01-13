package io.gumga.core.exception;

/**
 * Exception thrown when the resource is forbidden to the user
 */
public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }

}
