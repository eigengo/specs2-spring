package org.specs2.spring;

import org.specs2.spring.annotation.*;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author janmachacek
 */
class EnvironmentExtractor {

	Environment extract(Object o) {
		final Environment environment = new Environment();

		final Class<?> clazz = o.getClass();
		final Jndi annotation = AnnotationUtils.findAnnotation(clazz, Jndi.class);
		if (annotation != null) {
			environment.addDataSources(annotation.dataSources());
			environment.addTransactionManagers(annotation.transactionManager());
			environment.addMailSessions(annotation.mailSessions());
			environment.addJmsBrokers(annotation.jms());
			environment.addBeans(annotation.beans());
			environment.setBuilder(annotation.builder());
		}

		environment.addDataSource(AnnotationUtils.findAnnotation(clazz, DataSource.class));
		environment.addTransactionManager(AnnotationUtils.findAnnotation(clazz, TransactionManager.class));
		environment.addMailSession(AnnotationUtils.findAnnotation(clazz, MailSession.class));
		environment.addBean(AnnotationUtils.findAnnotation(clazz, Bean.class));

		return environment;
	}

}
