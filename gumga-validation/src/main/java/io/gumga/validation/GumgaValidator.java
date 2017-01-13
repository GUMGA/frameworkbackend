package io.gumga.validation;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import io.gumga.validation.exception.InvalidEntityException;
import io.gumga.validation.validator.GumgaCommonValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

/**
 * Classe que realiza a validação em uma lista de {@link GumgaFieldValidator}
 * 
 */
public class GumgaValidator {

	private Errors errors;

	public GumgaValidator(Errors errors) {
		this.errors = errors;
	}

	public GumgaValidator(Object object) {
		this(object, object.getClass().getSimpleName().toLowerCase());
	}

	public GumgaValidator(Object object, String name) {
		this(new BeanPropertyBindingResult(object, name));
	}

	public GumgaValidator() {
		this(new MapBindingResult(Maps.newHashMap(), "object"));
	}

	@SafeVarargs
	public final <T> GumgaValidator check(String property, T value, GumgaFieldValidator<? super T>... validators) {
		for (GumgaFieldValidator<? super T> validator : validators) {
			Optional<GumgaValidationError> errorCode = validator.validate(value, this.errors);
			if (errorCode.isPresent()) {
				GumgaValidationError error = errorCode.get();
				this.errors.rejectValue(property, error.getCode(), error.getArgs(), null);
				break;
			}
		}
		return this;
	}

	@SafeVarargs
	protected final <T> GumgaValidator check(String property, T value, String message,
			GumgaFieldValidator<? super T>... validators) {
		for (GumgaFieldValidator<? super T> validator : validators) {
			Optional<GumgaValidationError> errorCode = validator.validate(value, this.errors);
			if (errorCode.isPresent()) {
				GumgaValidationError error = errorCode.get();
				this.errors.rejectValue(property, error.getCode(), error.getArgs(), message);
				break;
			}
		}
		return this;
	}

	public final GumgaValidator checkIsTrue(String property, Boolean value) {
		return check(property, value, GumgaCommonValidator.isTrue());
	}

	public final GumgaValidator checkIsFalse(String property, Boolean value) {
		return check(property, value, GumgaCommonValidator.isFalse());
	}

	public final GumgaValidator checkNotNull(String property, Object value) {
		return check(property, value, GumgaCommonValidator.notNull());
	}

	public final GumgaValidator checkIsTrue(String property, Boolean value, String message) {
		return check(property, value, message, GumgaCommonValidator.isTrue());
	}

	public final GumgaValidator checkIsFalse(String property, Boolean value, String message) {
		return check(property, value, message, GumgaCommonValidator.isFalse());
	}

	public final GumgaValidator checkNotNull(String property, Object value, String message) {
		return check(property, value, message, GumgaCommonValidator.notNull());
	}

	public static final GumgaValidator with(Errors errors) {
		return new GumgaValidator(errors);
	}

	public static final GumgaValidator get() {
		return new GumgaValidator();
	}

	public void check() {
		if (this.errors.hasErrors()) {
			StringBuilder message = new StringBuilder();
			for (FieldError error : this.errors.getFieldErrors()) {
				message.append("field:" + error.getField() + " message:" + error.getDefaultMessage() + ";");
			}
			throw new InvalidEntityException(this.errors, message.toString());
		}
	}

}
