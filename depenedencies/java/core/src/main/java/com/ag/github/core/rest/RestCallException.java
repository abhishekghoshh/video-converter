package com.ag.github.core.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RestCallException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	private String description;

	public RestCallException(String message, HttpStatusCode httpStatusCode, String description) {
		super(message);
		this.message = message;
		this.httpStatus = httpStatusCode;
		this.description = description;
	}

	public RestCallException(String message, String description) {
		super(message);
		this.message = message;
		this.description = description;
	}

	public HttpStatusCode getHttpStatus() {
		return httpStatus;
	}

}
