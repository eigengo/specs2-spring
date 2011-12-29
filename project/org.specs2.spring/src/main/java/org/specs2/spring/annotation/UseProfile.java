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
public @interface UseProfile {

	/**
	 * Define the profiles that the test should load/use
	 * @return the profile names
	 */
	String[] value();
	
}
