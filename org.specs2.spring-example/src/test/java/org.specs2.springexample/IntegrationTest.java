package org.specs2.springexample;

import org.hsqldb.jdbc.JDBCDriver;
import org.specs2.spring.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author janmachacek
 */
@Jndi(
		dataSources = {
				@DataSource(name = "java:comp/env/jdbc/test",
					driverClass = JDBCDriver.class, url = "jdbc:hsqldb:mem:test"),
				@DataSource(name = "java:comp/env/jdbc/external",
					driverClass = JDBCDriver.class, url = "jdbc:hsqldb:mem:external")
		},
		mailSessions = @MailSession(name = "java:comp/env/mail/foo"),
		transactionManager = @TransactionManager(name = "java:comp/TransactionManager"),
		jms = @Jms(
				connectionFactoryName = "java:comp/env/jms/connectionFactory",
				queues = {@JmsQueue(name = "java:comp/env/jms/requests"),
						  @JmsQueue(name = "java:comp/env/jms/responses")},
				topics = {@JmsTopic(name = "java:comp/env/jms/cacheFlush"),
						  @JmsTopic(name = "java:comp/env/jms/ruleUpdate")}
		),
		workManagers = @WorkManager(name = "java:comp/env/work/WorkManager", kind = WorkManager.Kind.CommonJ,
			maximumThreads = 5, minimumThreads = 3)
)
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration("classpath*:/META-INF/spring/module-context.xml")
@UseProfile("ACU")
@SystemEnvironment({"efoo=bar;ebaz=null"})
@SystemProperties({"pfoo=bar;pbaz=null"})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegrationTest {
}
