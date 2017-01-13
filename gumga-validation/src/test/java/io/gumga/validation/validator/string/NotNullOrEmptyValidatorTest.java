package io.gumga.validation.validator.string;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaValidationError;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class NotNullOrEmptyValidatorTest {

	private static final String ERROR_CODE = "validation.string.notNullOrEmpty";
	private static final Errors errors = mock(Errors.class);

	@Test
	public void should_be_valid_if_have_filled_value() {
		Optional<GumgaValidationError> result;

		result = new NotNullOrEmptyValidator().validate("valor preenchido", errors);
		assertFalse(result.isPresent());

	}

	@Test
	public void should_be_valid_if_has_null_or_empty_value() {
		Optional<GumgaValidationError> result;

		result = new NotNullOrEmptyValidator().validate(null, errors);
		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertArrayEquals(new Object[] {} , result.get().getArgs());
		
		result = new NotNullOrEmptyValidator().validate("", errors);
		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertArrayEquals(new Object[] {} , result.get().getArgs());

		result = new NotNullOrEmptyValidator().validate("         ", errors);
		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertArrayEquals(new Object[] {} , result.get().getArgs());
	}
}
