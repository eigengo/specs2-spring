package org.specs2.spring;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception that gets thrown when the JNDI environment for the test cannot be created. This is usually fatal,
 * the test cannot proceed.
 *
 * @author janmachacek
 */
public class EnvironmentCreationException extends NestedRuntimeException {
	private static final long serialVersionUID = 5152661314945735372L;

	public EnvironmentCreationException(String msg) {
		super(msg);
	}

	public EnvironmentCreationException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
