package org.specs2.spring.annotation;

/**
 * Specifies a JMS topic at the given JNDI name
 *
 * @author janm
 */
public @interface JmsTopic {
	/**
	 * The name in the JNDI environment; typically something like <code>java:comp/env/jms/xyz</code>
	 *
	 * @return the JNDI name
	 */
	String name();
}
