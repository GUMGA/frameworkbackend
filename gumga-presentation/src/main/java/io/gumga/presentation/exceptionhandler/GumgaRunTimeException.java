package io.gumga.presentation.exceptionhandler;


import org.springframework.http.HttpStatus;

public class GumgaRunTimeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public GumgaRunTimeException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public GumgaRunTimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
