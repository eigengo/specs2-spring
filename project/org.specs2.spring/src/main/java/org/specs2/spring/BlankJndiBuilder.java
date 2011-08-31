package org.specs2.spring;

import java.util.Map;

/**
 * No-op JNDI builder; does not contribute anything to the environment.
 *
 * @author janm
 */
class BlankJndiBuilder implements JndiBuilder {

	public BlankJndiBuilder() {

	}

	public void build(Map<String, Object> environment) throws Exception {
		// do nothing
	}
}
