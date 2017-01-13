package io.gumga.validation.validator.common;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaFieldValidator;
import io.gumga.validation.GumgaValidationError;
import io.gumga.validation.validator.GumgaAbstractValidator;
import org.springframework.validation.Errors;

/**
 * Validate if the specified value is True
 */
public class IsTrueValidator extends GumgaAbstractValidator<Boolean> {

	public static final String ERROR_CODE = "validation.isTrue";
	public static final GumgaFieldValidator<Boolean> INSTANCE = new IsTrueValidator(ERROR_CODE);

	public IsTrueValidator(String code) {
		super(code);
	}

	@Override
	public Optional<GumgaValidationError> validate(Boolean value, Errors errors) {
		return doValidation(value);
	}

}
