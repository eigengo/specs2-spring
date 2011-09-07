package org.specs2.spring;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author janmachacek
 */
class TestTransactionDefinitionExtractor {

	TestTransactionDefinition extract(Object specification) {
		final Transactional transactional = AnnotationUtils.findAnnotation(specification.getClass(), Transactional.class);
		final TransactionConfiguration transactionConfiguration = AnnotationUtils.findAnnotation(specification.getClass(), TransactionConfiguration.class);
		if (transactional == null) return TestTransactionDefinition.NOT_TRANSACTIONAL;
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		transactionDefinition.setName("Test transaction");
		boolean defaultRollback = true;
		if (transactionConfiguration != null) defaultRollback = transactionConfiguration.defaultRollback();

		return new TestTransactionDefinition(transactionDefinition, defaultRollback);
	}

	static class TestTransactionDefinition {
		private final TransactionDefinition transactionDefinition;
		private final boolean defaultRollback;
		final static TestTransactionDefinition NOT_TRANSACTIONAL = new TestTransactionDefinition(null, false);

		TestTransactionDefinition(TransactionDefinition transactionDefinition, boolean defaultRollback) {
			this.transactionDefinition = transactionDefinition;
			this.defaultRollback = defaultRollback;
		}

		TransactionDefinition getTransactionDefinition() {
			return transactionDefinition;
		}

		boolean isDefaultRollback() {
			return defaultRollback;
		}
	}

}
