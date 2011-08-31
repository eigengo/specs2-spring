package org.specs2.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author janmachacek
 */
class TestContextCreator {

	void createAndAutowire(ContextConfiguration contextConfiguration, Object specification) {
		ApplicationContext context = new ClassPathXmlApplicationContext(contextConfiguration.value());
		context.getAutowireCapableBeanFactory().autowireBean(specification);
	}

}
