package org.specs2.spring.web;

import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

/**
 * @author janm
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebContextConfiguration {

	String webXml() default "/WEB-INF/web.xml";

	String[] value() default {};

	ContextConfiguration contextConfiguration();

	boolean useSpringSecurity() default false;


}
