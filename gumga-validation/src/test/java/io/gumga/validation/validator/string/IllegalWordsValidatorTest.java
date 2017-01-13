package io.gumga.validation.validator.string;

import com.google.common.base.Optional;
import io.gumga.validation.GumgaValidationError;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class IllegalWordsValidatorTest {

	private static final String ERROR_CODE = "validation.illegalText";
	private static final Errors errors = mock(Errors.class);

	@Test
	public void should_be_valid_if_have_not_illegal_words() {
		Optional<GumgaValidationError> result;

		result = new IllegalWordsValidator("teste1", "teste2").validate("nao contem palavras ilegais", errors);
		assertFalse(result.isPresent());

		result = new IllegalWordsValidator().validate("nao contem palavras ilegais", errors);
		assertFalse(result.isPresent());
	}

	@Test
	public void should_be_valid_if_has_illegal_words() {
		Optional<GumgaValidationError> result;

		result = new IllegalWordsValidator("palavras", "ilegais").validate("contem palavras ilegais", errors);
		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertArrayEquals(new Object[] {"palavras"} , result.get().getArgs());

		result = new IllegalWordsValidator("nao ilegal", "ilegais").validate("contem palavras ilegais", errors);
		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertArrayEquals(new Object[] {"ilegais"} , result.get().getArgs());
		
		result = new IllegalWordsValidator("PALAVRAS", "ILEGAIS").validate("contem palavras ilegais", errors);
		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertArrayEquals(new Object[] {"PALAVRAS"} , result.get().getArgs());
	}
}
