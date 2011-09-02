package org.specs2.spring;

import org.specs2.spring.annotation.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/**
 * Extracts the {@link Environment} from the test specification. It first tries to locate the {@link Jndi} annotation
 * (on the test class itself or on any annotation of its annotations).
 *
 * @author janmachacek
 */
class EnvironmentExtractor {

	/**
	 * Extracts the environment from the test object.
	 *
	 * @param specification the test object; never {@code null}.
	 * @return the extracted Environment object.
	 */
	Environment extract(Object specification) {
		Assert.notNull(specification, "The 'specification' argument cannot be null.");

		final Environment environment = new Environment();

		final Class<?> clazz = specification.getClass();
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
