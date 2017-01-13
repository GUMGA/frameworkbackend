package io.gumga.presentation;


public class RestResponse<T> {

	private String code;
	private String message;
	private String details;
	private T data;
	
	public RestResponse(String message) {
		this.message = message;
	}

	public RestResponse(String code, String message) {
		this(message);
		this.code = code;
	}
	
	public RestResponse(String code, String message, String details) {
		this(code, message);
		this.details = details;
	}
	
	public RestResponse(T data) {
		this.data = data;
	}

	public RestResponse(T data, String message) {
		this.data = data;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
