package io.gumga.validation.validator.common;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaValidationError;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class IsFalseValidatorTest {

	private static final String ERROR_CODE = "error code";
	private static final Errors errors = mock(Errors.class);

	@Test
	public void should_be_invalid_if_value_is_true() {

		Optional<GumgaValidationError> result = new IsTrueValidator(ERROR_CODE).validate(Boolean.FALSE, errors);

		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertNotNull(result.get().getArgs());
		assertEquals(0, result.get().getArgs().length);
	}

	@Test
	public void should_be_valid_if_value_is_false() {
		Optional<GumgaValidationError> result = new IsTrueValidator(ERROR_CODE).validate(Boolean.TRUE, errors);
		assertFalse(result.isPresent());

	}
}
