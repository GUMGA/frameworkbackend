package io.gumga.validation.validator.common;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaValidationError;
import io.gumga.validation.validator.GumgaAbstractValidator;
import org.springframework.validation.Errors;

/**
 * Validate if the specified value is not <code>null</code>
 */
public class NotNullValidator extends GumgaAbstractValidator<Object> {

	public static final String ERROR_CODE = "validation.notNull";
	public static final NotNullValidator INSTANCE = new NotNullValidator(ERROR_CODE);

	public NotNullValidator(String code) {
		super(code);
	}

	@Override
	public Optional<GumgaValidationError> validate(Object value, Errors errors) {
		return doValidation(value != null);
	}

}
