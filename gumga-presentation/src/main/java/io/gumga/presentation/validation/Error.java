package io.gumga.presentation.validation;

public enum Error {
	
	NO_ERROR("0"), INVALID_ENTITY("1");

	private String code;
	
	private Error(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
}
