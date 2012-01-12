package org.specs2.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author janmachacek
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Property {
	/**
	 * The name of the property
	 * @return the name; never {@code null}
	 */
	String name();

	/**
	 * The value of the property; if you want {@code null}, use {@link org.specs2.spring.annotation.SystemEnvironment#nullValue()}
	 * @return the value; never {@code null}
	 */
	String value();
}
