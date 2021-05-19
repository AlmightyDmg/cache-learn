package com.haizhi.databridge.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class ValidationErrorException extends ReturnToHttpException {

	private static final long serialVersionUID = 6333241510540528286L;

	private Map<String, Object> errors;
	
	public ValidationErrorException(Map<String, Object> errors, String message) {
		super(HttpStatus.BAD_REQUEST.value(), errors, message);
		this.errors = errors;
	}
	
	public Map<String, Object> getErrors() {
		return errors;
	}
}
