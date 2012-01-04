package org.specs2.spring.web;

import org.springframework.core.NestedRuntimeException;

/**
 * @author janm
 */
public class WebTestContextCreationException extends NestedRuntimeException {
	private static final long serialVersionUID = 8958108257857150162L;

	public WebTestContextCreationException(String msg) {
		super(msg);
	}

	public WebTestContextCreationException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
