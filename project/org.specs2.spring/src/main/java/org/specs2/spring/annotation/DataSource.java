package org.specs2.spring.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Driver;

/**
 * Specifies the {@link javax.activation.DataSource} to be added to the JNDI environment
 *
 * @author janm
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DataSource {

	/**
	 * The name in the JNDI environment; typically something like <code>java:comp/env/bean/xyz</code>
	 *
	 * @return the JNDI name
	 */
	String name();

	/**
	 * Specifies the type of the JDBC driver
	 *
	 * @return the driver class
	 */
	Class<? extends Driver> driverClass();

	/**
	 * The JDBC URL
	 *
	 * @return the URL
	 */
	String url();

	/**
	 * The username to establish the DB connection
	 *
	 * @return the username
	 */
	String username() default "sa";

	/**
	 * The password to establish the DB connection
	 *
	 * @return the password
	 */
	String password() default "";

}
