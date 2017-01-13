package io.gumga.presentation.validation;

import io.gumga.presentation.RestResponse;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ErrorResource extends RestResponse<Object> {

	private List<FieldErrorResource> fieldErrors = new LinkedList<>();
	
	public static final ErrorResource NO_ERRORS = new ErrorResource(Error.NO_ERROR, "Success / No Error.");

	public ErrorResource(String code, String message) {
		super(code, message);
	}
	
	public ErrorResource(Error error, String message) {
		super(error.getCode(), message);
	}

	public ErrorResource(String code, String message, String details) {
		super(code, message, details);
	}

	public List<FieldErrorResource> getFieldErrors() {
		return Collections.unmodifiableList(fieldErrors);
	}
	
	public void addFieldError(FieldErrorResource fieldError) {
		fieldErrors.add(fieldError);
	}

	public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}
