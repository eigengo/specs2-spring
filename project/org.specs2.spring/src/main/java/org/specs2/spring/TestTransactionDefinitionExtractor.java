package org.specs2.spring;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

/**
 * Determines transactional configuration for a given specification by looking at its annotations.
 *
 * @author janmachacek
 */
public class TestTransactionDefinitionExtractor {

    /**
     * Examines the {@code specification}'s annotations to prepare the {@code TestTransactionDefinition} object
     * that describes whether and how the specification's examples should be run.
     *
     * @param specification the specification to examine, never {@code null}.
     * @return the {@code TestTransactionDefinition} for the specification, never {@code null}.
     */
    public TestTransactionDefinition extract(Object specification) {
        Assert.notNull(specification, "The 'specification' argument cannot be null.");

        final Transactional transactional = AnnotationUtils.findAnnotation(specification.getClass(), Transactional.class);
        final TransactionConfiguration transactionConfiguration = AnnotationUtils.findAnnotation(specification.getClass(), TransactionConfiguration.class);
        if (transactional == null) return TestTransactionDefinition.NOT_TRANSACTIONAL;
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionDefinition.setName("Test transaction");
        boolean defaultRollback = true;
        String transactionManagerName = "transactionManager";
        if (transactionConfiguration != null) {
            defaultRollback = transactionConfiguration.defaultRollback();
            transactionManagerName = transactionConfiguration.transactionManager();
        }

        return new TestTransactionDefinition(transactionDefinition, transactionManagerName, defaultRollback);
    }

    /**
     * Contains details of the example's expected transactional behaviour. The {@link #transactionDefinition} is the
     * definition that the {@code PlatformTransactionManager} will use to obtain the transaction; the {@link #defaultRollback}
     * indicates whether the transaction should be rolled back at the end of the example execution.
     */
    public static class TestTransactionDefinition {
        private final TransactionDefinition transactionDefinition;
        private final boolean defaultRollback;
        private final String transactionManagerName;
        public final static TestTransactionDefinition NOT_TRANSACTIONAL = new TestTransactionDefinition(null, null, false);

        TestTransactionDefinition(TransactionDefinition transactionDefinition, String transactionManagerName, boolean defaultRollback) {
            this.transactionDefinition = transactionDefinition;
            this.transactionManagerName = transactionManagerName;
            this.defaultRollback = defaultRollback;
        }

        public TransactionDefinition getTransactionDefinition() {
            return transactionDefinition;
        }

        public boolean isDefaultRollback() {
            return defaultRollback;
        }

        public String getTransactionManagerName() {
            return transactionManagerName;
        }
    }

}
