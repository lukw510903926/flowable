package com.flowable.oa.util.exception;

public class NullableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NullableException() {
	}

	public NullableException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullableException(String message) {
		super(message);
	}

	public NullableException(Throwable cause) {
		super(cause);
	}
}
