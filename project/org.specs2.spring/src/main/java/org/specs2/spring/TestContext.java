package org.specs2.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfiguration;

/**
 * Creates Spring ApplicationContext for the test
 *
 * @author janmachacek
 */
class TestContext {

	private ApplicationContext context;

	/**
	 * Creates the {@link ApplicationContext} and autowire the fields / setters test object.
	 *
	 * @param specification the specification object to set up.
	 */
	void createAndAutowire(Object specification) {
		final ContextConfiguration contextConfiguration = AnnotationUtils.findAnnotation(specification.getClass(), ContextConfiguration.class);
		if (contextConfiguration == null) return;

		this.context = new ClassPathXmlApplicationContext(contextConfiguration.value());
		this.context.getAutowireCapableBeanFactory().autowireBean(specification);
	}

	<T> T getBean(Class<T> beanType) {
		return this.context.getBean(beanType);
	}

}
