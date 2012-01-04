package org.specs2.spring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies the JNDI-bound {@link javax.mail.Session}.
 *
 * @author janmmachacek
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MailSession {

	/**
	 * The name in the JNDI environment; typically something like <code>java:comp/env/bean/xyz</code>
	 *
	 * @return the JNDI name
	 */
	String name();

	/**
	 * The JavaMail properties for the session; for example <code>mail.smtp.host=localhost</code>
	 *
	 * @return the JavaMail properties
	 */
	String[] properties() default {};

}
