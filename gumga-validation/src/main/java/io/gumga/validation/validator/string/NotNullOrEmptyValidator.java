package io.gumga.validation.validator.string;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaValidationError;
import io.gumga.validation.validator.GumgaAbstractValidator;
import org.springframework.validation.Errors;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;

/**
 * Validate is a String is not null or empty
 * 
 */
public class NotNullOrEmptyValidator extends GumgaAbstractValidator<String> {

	public static final String ERROR_CODE = "validation.string.notNullOrEmpty";

	public NotNullOrEmptyValidator() {
		super(ERROR_CODE);
	}

	@Override
	public Optional<GumgaValidationError> validate(String value, Errors errors) {
		return this.doValidation(!isNullOrEmpty(nullToEmpty(value).trim()));
	}

}
