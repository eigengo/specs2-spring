package org.specs2.spring;

import org.specs2.spring.annotation.DataSource;
import org.specs2.spring.annotation.Jndi;
import org.specs2.spring.annotation.TransactionManager;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author janmachacek
 */
class EnvironmentExtractor {

	Environment extract(Object o) {
		final Environment environment = new Environment();

		final Class<? extends Object> clazz = o.getClass();
		final Jndi annotation = AnnotationUtils.findAnnotation(clazz, Jndi.class);
		if (annotation != null) {
			environment.addDataSources(annotation.dataSources());
			environment.addTransactionManagers(annotation.transactionManager());
		}

		environment.addDataSource(AnnotationUtils.findAnnotation(clazz, DataSource.class));
		environment.addTransactionManager(AnnotationUtils.findAnnotation(clazz, TransactionManager.class));

		return environment;
	}

}
