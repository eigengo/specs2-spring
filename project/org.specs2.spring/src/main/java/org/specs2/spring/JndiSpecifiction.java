package org.specs2.spring;

import org.specs2.spring.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author janmachacek
 */
class JndiSpecifiction {
	private final List<DataSource> dataSources = new ArrayList<DataSource>();
	private final List<MailSession> mailSessions = new ArrayList<MailSession>();
	private final List<TransactionManager> transactionManagers = new ArrayList<TransactionManager>();
	private final List<JmsQueue> jmsQueues = new ArrayList<JmsQueue>();
	private final List<JmsTopic> jmsTopics = new ArrayList<JmsTopic>();
	private final List<Bean> beans = new ArrayList<Bean>();
	// private List<WorkManager> workManagers = new ArrayList<WorkManager>();

	void detect(Object object) {

	}

	void detect(Jndi jndi) {
		addAll(this.dataSources, jndi.dataSources());
		addAll(this.mailSessions, jndi.mailSessions());
		addAll(this.transactionManagers, jndi.transactionManager());

		addAll(this.beans, jndi.beans());
	}

	private <T> void addAll(List<T> target, T[] source) {
		for (int i = 0; i < source.length; i++) target.add(source[i]);
	}
}
