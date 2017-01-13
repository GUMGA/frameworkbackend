package io.gumga.validation.validator.collection;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaValidationError;
import io.gumga.validation.validator.GumgaAbstractValidator;
import org.springframework.validation.Errors;

import java.util.Collection;

/**
 * Validate is the {@link Collection} is not empty
 * 
 */
public class NotEmptyCollectionValidator extends GumgaAbstractValidator<Collection<?>> {

	public static final String ERROR_CODE = "validation.collection.notNullOrEmpty";

	public NotEmptyCollectionValidator() {
		super(ERROR_CODE);
	}

	@Override
	public Optional<GumgaValidationError> validate(Collection<?> value, Errors errors) {
		return this.doValidation(value != null && !value.isEmpty());
	}

}
