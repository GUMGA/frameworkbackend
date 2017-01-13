package io.gumga.validation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Classe que determina um erro de validação
 */
public class GumgaValidationError {

	private String code;
	private Object[] args;

	public GumgaValidationError(String code, Object[] args) {
		checkArgument(!isNullOrEmpty(code), "O código de erro deve ser especificado.");
		this.code = code;
		this.args = args;
	}

	public GumgaValidationError(String code) {
		this(code, null);
	}

	public String getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}

}
