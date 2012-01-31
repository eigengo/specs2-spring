package org.specs2.spring.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies an arbitrary object to be added to the JNDI environment.
 *
 * @author janmachacek
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

	/**
	 * The name in the JNDI environment; typically something like <code>java:comp/env/bean/xyz</code>
	 *
	 * @return the JNDI name
	 */
	String name();

	/**
	 * The type of the object; the type must have an accessible nullary constructor.
	 *
	 * @return the object name
	 */
	Class<?> clazz();

}
