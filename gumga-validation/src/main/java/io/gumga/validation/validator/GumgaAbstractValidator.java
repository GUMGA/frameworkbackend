package io.gumga.validation.validator;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaFieldValidator;
import io.gumga.validation.GumgaValidationError;

/**
 * Helper class to validations
 * 
 * @param <T>
 */
public abstract class GumgaAbstractValidator<T> implements GumgaFieldValidator<T> {

	private String code;

	public GumgaAbstractValidator(String message) {
		this.code = message;
	}

	protected Optional<GumgaValidationError> doValidation(boolean isValid) {
		return this.doValidation(isValid, new Object[] {});
	}

	protected Optional<GumgaValidationError> doValidation(boolean isValid, Object[] args) {
		return isValid ? Optional.<GumgaValidationError> absent() : Optional.of(new GumgaValidationError(code, args));
	}

}
