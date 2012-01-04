package org.specs2.spring.annotation;

/**
 * Specifies JMS configuration
 *
 * @author janmmachacek
 */
public @interface Jms {

	/**
	 * The name in the JNDI environment; typically something like <code>java:comp/env/jms/connectionFactory</code>
	 *
	 * @return the JNDI name
	 */
	String connectionFactoryName();

	/**
	 * Specifies any number of JMS queues managed by this connection factory
	 *
	 * @return the queues
	 */
	JmsQueue[] queues() default {};

	/**
	 * Specifies any number of JMS topics managed by this connection factory
	 *
	 * @return the topics
	 */
	JmsTopic[] topics() default {};

}
