package com.dwl.dbhelp.exception;

public class DbHelpException extends RuntimeException {

	public DbHelpException(String message) {
		super(message);
	}

	public DbHelpException(String message, Throwable cause) {
		super(message, cause);
	}
}
