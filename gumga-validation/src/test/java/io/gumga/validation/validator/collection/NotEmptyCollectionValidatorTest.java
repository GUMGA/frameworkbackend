package io.gumga.validation.validator.collection;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.gumga.validation.GumgaValidationError;
import org.junit.Test;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class NotEmptyCollectionValidatorTest {

	private Errors errors = mock(Errors.class);

	@Test
	public void should_be_invalid_if_the_specified_field_is_empty() {


		Optional<GumgaValidationError> result;

		result = new NotEmptyCollectionValidator().validate(new ArrayList<>(), errors);
		assertTrue(result.isPresent());
		assertEquals(NotEmptyCollectionValidator.ERROR_CODE, result.get().getCode());
		
		result = new NotEmptyCollectionValidator().validate(new HashSet<>(), errors);
		assertTrue(result.isPresent());
		assertEquals(NotEmptyCollectionValidator.ERROR_CODE, result.get().getCode());

		result = new NotEmptyCollectionValidator().validate(null, errors);
		assertTrue(result.isPresent());
		assertEquals(NotEmptyCollectionValidator.ERROR_CODE, result.get().getCode());

	}

	@Test
	public void should_be_valid_if_the_specified_field_has_no_error() {

		Optional<GumgaValidationError> result;

		result = new NotEmptyCollectionValidator().validate(Lists.newArrayList("elemento1", "elemento2"), errors);
		assertFalse(result.isPresent());
		
		result = new NotEmptyCollectionValidator().validate(Sets.newHashSet("elemento1", "elemento2"), errors);
		assertFalse(result.isPresent());

	}
	
}
