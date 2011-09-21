package org.specs2.spring.web;

import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author janm
 */
class DispatcherServletHolder {
	private static final ThreadLocal<DispatcherServlet> DISPATCHER_SERVLET = new ThreadLocal<DispatcherServlet>();

	static void set(DispatcherServlet dispatcherServlet) {
		DISPATCHER_SERVLET.set(dispatcherServlet);
	}

	static DispatcherServlet get() {
		return DISPATCHER_SERVLET.get();
	}
}
