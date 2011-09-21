package org.specs2.spring.web;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextLoader;

/**
 * @author janm
 */
public class ExistingApplicationContextLoader implements ContextLoader {

	@Override
	public String[] processLocations(Class<?> clazz, String... locations) {
		return new String[0];
	}

	@Override
	public ApplicationContext loadContext(String... locations) throws Exception {
		return DispatcherServletHolder.get().getWebApplicationContext();
	}
}
