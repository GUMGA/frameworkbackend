package io.gumga.validation.validator.common;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaFieldValidator;
import io.gumga.validation.GumgaValidationError;
import org.springframework.validation.Errors;

/**
 * Validate if the field has error
 */
public class FieldWithoutErrorsValidator implements GumgaFieldValidator<Object> {

	public static final String ERROR_CODE = "validation.fieldWithoutErrors";

	private String field;
	private String code;

	public FieldWithoutErrorsValidator(String field) {
		this(field, ERROR_CODE);
	}

	public FieldWithoutErrorsValidator(String field, String code) {
		this.field = field;
		this.code = code;
	}

	@Override
	public Optional<GumgaValidationError> validate(Object value, Errors errors) {
		if (errors.hasFieldErrors(this.field))
			return Optional.of(new GumgaValidationError(code, new Object[] { this.field }));

		return Optional.<GumgaValidationError> absent();
	}
}
