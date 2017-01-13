package io.gumga.core.exception;

/**
 * Exception thrown when has error on request
 */
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

}
