package io.gumga.presentation.exceptionhandler;


import io.gumga.presentation.validation.FieldErrorResource;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GumgaRunTimeException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final List<FieldErrorResource> fieldErrors;

    public GumgaRunTimeException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.fieldErrors = null;
    }

    public GumgaRunTimeException(HttpStatus httpStatus, List<FieldErrorResource> fieldErrors) {
        this.httpStatus = httpStatus;
        this.fieldErrors = fieldErrors;
    }

    public GumgaRunTimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.fieldErrors = null;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<FieldErrorResource> getFieldErrors() {
        return fieldErrors;
    }
}
