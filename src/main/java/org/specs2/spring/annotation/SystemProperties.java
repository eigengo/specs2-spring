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
public @interface SystemProperties {
	/**
	 * Indicates whether the properties specified entries should overwrite the existing values. When
	 * {@code false} the existing values will be kept.
	 * @return {@code true} if the existing values should be overwritten.
	 */
	boolean overwrite() default true;

	/**
	 * Indicates whether the existing environment should be cleared.
	 * @return {@code true} if the environment should be cleared.
	 */
	boolean clear() default false;

	/**
	 * Value that, when used in {@link Property#value()} or in the {@code value} portion of the {@link #value()} will be interpreted
	 * as {@code null} rather than a {@code String} with value {@code "null"}.
	 * @return the {@code null} name.
	 */
	String nullValue() default "null";

	/**
	 * The properties
	 * @return the properties
	 */
	Property[] properties() default {};

	/**
	 * The properties in <code>{name<sub>1</sub>=value<sub>1</sub>, name<sub>2</sub>=value<sub>2</sub>, ..., name<sub>n</sub>=value<sub>n</sub>}</code>
	 * syntax.
	 * @return the properties.
	 */
	String[] value() default {};

}
