package com.moviebook.util;

public class InvalidPropertyException extends Exception {

	public InvalidPropertyException() {
		super();
	}

	public InvalidPropertyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidPropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPropertyException(String message) {
		super(message);
	}

	public InvalidPropertyException(Throwable cause) {
		super(cause);
	}

}
