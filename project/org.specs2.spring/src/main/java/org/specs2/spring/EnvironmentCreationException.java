package org.specs2.spring;

import org.springframework.core.NestedRuntimeException;

/**
 * @author janm
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
