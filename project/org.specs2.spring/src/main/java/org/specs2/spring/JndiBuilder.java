package org.specs2.spring;

import java.util.Map;

/**
 * Performs custom processing to contribute items to the JNDI environment.
 *
 * @author janm
 */
public interface JndiBuilder {

	/**
	 * Adds the items into the environment; the key in the map is the JNDI name; the value is the
	 * object associated with that name.
	 *
	 * @param environment the environment to be added to; never {@code null}.
	 * @throws Exception if the environment could not be built.
	 */
	void build(Map<String, Object> environment) throws Exception;

}
