package org.specs2.spring.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies the {@link javax.transaction.TransactionManager} to be added to the JNDI environment
 *
 * @author janm
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TransactionManager {
	/**
	 * The name in the JNDI environment; typically something like <code>java:comp/env/TransactionManager</code>
	 *
	 * @return the JNDI name
	 */
	String name();

}
