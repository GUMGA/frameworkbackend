package io.gumga.validation;

import io.gumga.validation.exception.InvalidEntityException;
import io.gumga.validation.validator.GumgaCommonValidator;
import io.gumga.validation.validator.GumgaStringValidation;
import io.gumga.validation.validator.common.IsFalseValidator;
import io.gumga.validation.validator.common.IsTrueValidator;
import io.gumga.validation.validator.common.NotNullValidator;
import io.gumga.validation.validator.string.NotNullOrEmptyValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Date;

import static org.junit.Assert.*;

public class GumgaValidatorTest {

	private static final String NAME_FIELD = "name";
	private static final String BIRTH_DATE_FIELD = "birthDate";
	private Errors errors = Mockito.mock(Errors.class);

	@Before
	public void setUp() {
		Person person = Mockito.mock(Person.class);
		this.errors = new BeanPropertyBindingResult(person, "person");
	}

	@Test
	public void should_be_valid_if_all_validations_is_true() {

		GumgaValidator.with(errors) //
				.check(BIRTH_DATE_FIELD, new Date(), GumgaCommonValidator.notNull()) //
				.check(NAME_FIELD, "any name", GumgaStringValidation.notNullOrEmpty());

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(NAME_FIELD));
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_any_validation_is_false() {

		GumgaValidator.with(errors) //
				.check(BIRTH_DATE_FIELD, new Date(), GumgaCommonValidator.notNull()) //
				.check(NAME_FIELD, "", GumgaStringValidation.notNullOrEmpty());

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(NAME_FIELD));
		assertEquals(NotNullOrEmptyValidator.ERROR_CODE, errors.getFieldError(NAME_FIELD).getCode());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_expression_is_true() {

		GumgaValidator.with(errors) //
				.checkIsFalse(BIRTH_DATE_FIELD, true);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(BIRTH_DATE_FIELD));
		Assert.assertEquals(IsFalseValidator.ERROR_CODE, errors.getFieldError(BIRTH_DATE_FIELD).getCode());

	}

	@Test
	public void should_be_valid_if_expression_is_false() {

		GumgaValidator.with(errors) //
				.checkIsFalse(BIRTH_DATE_FIELD, false);

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_expression_is_false() {

		GumgaValidator.with(errors) //
				.checkIsTrue(BIRTH_DATE_FIELD, false);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(BIRTH_DATE_FIELD));
		assertEquals(IsTrueValidator.ERROR_CODE, errors.getFieldError(BIRTH_DATE_FIELD).getCode());

	}

	@Test
	public void should_be_valid_if_expression_is_true() {

		GumgaValidator.with(errors) //
				.checkIsTrue(BIRTH_DATE_FIELD, true);

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_expression_is_null() {

		GumgaValidator.with(errors) //
				.checkNotNull(BIRTH_DATE_FIELD, null);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(BIRTH_DATE_FIELD));
		Assert.assertEquals(NotNullValidator.ERROR_CODE, errors.getFieldError(BIRTH_DATE_FIELD).getCode());

	}

	@Test
	public void should_be_valid_if_expression_is_not_null() {

		GumgaValidator.with(errors) //
				.checkNotNull(BIRTH_DATE_FIELD, new Date());

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test(expected = InvalidEntityException.class)
	public void should_throw_exception_if_validation_is_false() {

		GumgaValidator.with(errors) //
				.check(BIRTH_DATE_FIELD, new Date(), GumgaCommonValidator.notNull()) //
				.check(NAME_FIELD, "", GumgaStringValidation.notNullOrEmpty()).check();

	}

	@Test(expected = InvalidEntityException.class)
	public void should_throw_exception_if_validation_is_false_and_have_no_errors() {

		GumgaValidator.get() //
				.checkNotNull(BIRTH_DATE_FIELD, new Date()) //
				.check(NAME_FIELD, "", GumgaStringValidation.notNullOrEmpty()).check();

	}
	
	@Test
	public void mustShowAMessageOfError() {	
	  try {
	    GumgaValidator
	      .get()
	      .check(NAME_FIELD, "", "The name field can't be equals empty", GumgaStringValidation.notNullOrEmpty())
	      .check();
	    Assert.fail();
	  } catch(InvalidEntityException e) {		
	    FieldError error = e.getErrors().getFieldError(NAME_FIELD);			
	    assertEquals(NAME_FIELD, error.getField()); 
	    assertEquals("The name field can't be equals empty", error.getDefaultMessage());
	  }
	}	

}
