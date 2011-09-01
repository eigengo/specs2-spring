package org.specs2.spring;

import org.specs2.spring.annotation.*;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

/**
 * @author janmachacek
 */
class Environment {
	private final List<DataSourceDefinition> dataSources = new ArrayList<DataSourceDefinition>();
	private final List<TransactionManagerDefinition> transactionManagers = new ArrayList<TransactionManagerDefinition>();
	private final List<MailSessionDefinition> mailSessions = new ArrayList<MailSessionDefinition>();
	private final List<JmsDefinition> jmsDefinitions = new ArrayList<JmsDefinition>();
	private final List<BeanDefinition> beans = new ArrayList<BeanDefinition>();
	private Class<? extends JndiBuilder> builder = BlankJndiBuilder.class;

	void addDataSource(DataSource dataSource) {
		if (dataSource == null) return;
		this.dataSources.add(new DataSourceDefinition(dataSource.name(), dataSource.driverClass(), dataSource.url(), dataSource.username(), dataSource.password()));
	}

	void addDataSources(DataSource... dataSources) {
		for (DataSource dataSource : dataSources) addDataSource(dataSource);
	}

	void addTransactionManager(TransactionManager transactionManager) {
		if (transactionManager == null) return;
		this.transactionManagers.add(new TransactionManagerDefinition(transactionManager.name()));
	}

	void addTransactionManagers(TransactionManager... transactionManagers) {
		for (TransactionManager transactionManager : transactionManagers) addTransactionManager(transactionManager);
	}

	void addMailSession(MailSession mailSession) {
		if (mailSession == null) return;
		this.mailSessions.add(new MailSessionDefinition(mailSession.name(), mailSession.properties()));
	}

	void addMailSessions(MailSession... mailSessions) {
		for (MailSession mailSession : mailSessions) addMailSession(mailSession);
	}

	public void addJmsBrokers(Jms[] jmses) {
		for (Jms jms : jmses) {
			final JmsDefinition jmsDefinition = new JmsDefinition(jms.connectionFactoryName());
			jmsDefinition.addQueues(jms.queues());
			jmsDefinition.addTopics(jms.topics());
			this.jmsDefinitions.add(jmsDefinition);
		}
	}

	void addBean(Bean bean) {
		if (bean == null) return;
		this.beans.add(new BeanDefinition(bean.name(), bean.type()));
	}

	void addBeans(Bean[] beans) {
		for (Bean bean : beans) addBean(bean);
	}

	Class<? extends JndiBuilder> getBuilder() {
		return builder;
	}

	void setBuilder(Class<? extends JndiBuilder> builder) {
		this.builder = builder;
	}

	List<DataSourceDefinition> getDataSources() {
		return dataSources;
	}

	List<TransactionManagerDefinition> getTransactionManagers() {
		return transactionManagers;
	}

	List<MailSessionDefinition> getMailSessions() {
		return mailSessions;
	}

	List<JmsDefinition> getJmsDefinitions() {
		return jmsDefinitions;
	}

	List<BeanDefinition> getBeans() {
		return beans;
	}

	static class TransactionManagerDefinition {
		private final String name;

		TransactionManagerDefinition(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}
	}

	static class BeanDefinition {
		private final String name;
		private final Class<?> type;

		BeanDefinition(String name, Class<?> type) {
			this.name = name;
			this.type = type;
		}

		String getName() {
			return name;
		}

		Class<?> getType() {
			return type;
		}
	}

	static class JmsDefinition {
		private final String connectionFactoryName;
		private final List<JmsQueueDefinition> jmsQueues = new ArrayList<JmsQueueDefinition>();
		private final List<JmsTopicDefinition> jmsTopics = new ArrayList<JmsTopicDefinition>();

		JmsDefinition(String connectionFactoryName) {
			this.connectionFactoryName = connectionFactoryName;
		}

		void addTopics(JmsTopic... jmsTopics) {
			for (JmsTopic topic : jmsTopics) {
				this.jmsTopics.add(new JmsTopicDefinition(topic.name()));
			}
		}

		void addQueues(JmsQueue... queues) {
			for (JmsQueue queue : queues) {
				this.jmsQueues.add(new JmsQueueDefinition(queue.name()));
			}
		}

		String getConnectionFactoryName() {
			return connectionFactoryName;
		}

		List<JmsQueueDefinition> getJmsQueues() {
			return jmsQueues;
		}

		List<JmsTopicDefinition> getJmsTopics() {
			return jmsTopics;
		}
	}

	static class JmsQueueDefinition {
		private final String name;

		JmsQueueDefinition(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}
	}

	static class JmsTopicDefinition {
		private final String name;

		JmsTopicDefinition(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}
	}

	static class MailSessionDefinition {
		private final String name;
		private final String[] properties;

		MailSessionDefinition(String name, String[] properties) {
			this.name = name;
			this.properties = properties;
		}

		String getName() {
			return name;
		}

		String[] getProperties() {
			return properties;
		}
	}

	static class DataSourceDefinition {
		private final String name;
		private final Class<? extends Driver> driverClass;
		private final String url;
		private final String username;
		private final String password;

		DataSourceDefinition(String name, Class<? extends Driver> driverClass, String url, String username, String password) {
			this.name = name;
			this.driverClass = driverClass;
			this.url = url;
			this.username = username;
			this.password = password;
		}

		String getName() {
			return name;
		}

		Class<? extends Driver> getDriverClass() {
			return driverClass;
		}

		String getUrl() {
			return url;
		}

		String getUsername() {
			return username;
		}

		String getPassword() {
			return password;
		}
	}
}
