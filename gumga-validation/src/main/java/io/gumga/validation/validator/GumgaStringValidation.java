package io.gumga.validation.validator;

import io.gumga.validation.GumgaFieldValidator;
import io.gumga.validation.validator.string.IllegalWordsValidator;
import io.gumga.validation.validator.string.NotNullOrEmptyValidator;

public class GumgaStringValidation {

	private GumgaStringValidation() { }

	public static final GumgaFieldValidator<String> containsIllegalWords(String... illegalWords) {
		return new IllegalWordsValidator(illegalWords);
	}

	public static final GumgaFieldValidator<String> notNullOrEmpty() {
		return new NotNullOrEmptyValidator();
	}

}
