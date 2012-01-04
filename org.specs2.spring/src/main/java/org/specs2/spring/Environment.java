package org.specs2.spring;

import org.specs2.spring.annotation.*;
import org.springframework.util.Assert;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the information that will be used as the source for the objects that will be added to the
 * JNDI environment.
 *
 * @author janmachacek
 */
class Environment {
	private final List<DataSourceDefinition> dataSources = new ArrayList<DataSourceDefinition>();
	private final List<TransactionManagerDefinition> transactionManagers = new ArrayList<TransactionManagerDefinition>();
	private final List<MailSessionDefinition> mailSessions = new ArrayList<MailSessionDefinition>();
	private final List<JmsDefinition> jmsDefinitions = new ArrayList<JmsDefinition>();
	private final List<BeanDefinition> beans = new ArrayList<BeanDefinition>();
	private Class<? extends JndiBuilder> builder = BlankJndiBuilder.class;

	/**
	 * Adds a DataSource
	 *
	 * @param dataSource the data source, or {@code null}.
	 */
	void addDataSource(DataSource dataSource) {
		if (dataSource == null) return;
		this.dataSources.add(new DataSourceDefinition(dataSource.name(), dataSource.driverClass(), dataSource.url(), dataSource.username(), dataSource.password()));
	}

	/**
	 * Adds all DataSources
	 *
	 * @param dataSources all data sources, never {@code null}.
	 */
	void addDataSources(DataSource... dataSources) {
		for (DataSource dataSource : dataSources) addDataSource(dataSource);
	}

	/**
	 * Adds a TransactionManager
	 *
	 * @param transactionManager the transaction manager or {@code null}.
	 */
	void addTransactionManager(TransactionManager transactionManager) {
		if (transactionManager == null) return;
		this.transactionManagers.add(new TransactionManagerDefinition(transactionManager.name()));
	}

	/**
	 * Adds all TransactionManagers
	 *
	 * @param transactionManagers all transaction managers, never {@code null}.
	 */
	void addTransactionManagers(TransactionManager... transactionManagers) {
		for (TransactionManager transactionManager : transactionManagers) addTransactionManager(transactionManager);
	}

	void addMailSession(MailSession mailSession) {
		if (mailSession == null) return;
		this.mailSessions.add(new MailSessionDefinition(mailSession.name(), mailSession.properties()));
	}


	/**
	 * Adds all mail sessions
	 *
	 * @param mailSessions all mail sessions, never {@code null}.
	 */
	void addMailSessions(MailSession... mailSessions) {
		for (MailSession mailSession : mailSessions) addMailSession(mailSession);
	}

	/**
	 * Add all JMS broker defintions
	 *
	 * @param jmses the jms definitions, never {@code null}.
	 */
	public void addJmsBrokers(Jms[] jmses) {
		for (Jms jms : jmses) {
			final JmsDefinition jmsDefinition = new JmsDefinition(jms.connectionFactoryName());
			jmsDefinition.addQueues(jms.queues());
			jmsDefinition.addTopics(jms.topics());
			this.jmsDefinitions.add(jmsDefinition);
		}
	}

	/**
	 * Adds a Bean definition
	 *
	 * @param bean the bean, or {@code null}.
	 */
	void addBean(Bean bean) {
		if (bean == null) return;
		this.beans.add(new BeanDefinition(bean.name(), bean.type()));
	}

	/**
	 * Adds all Bean definitions
	 *
	 * @param beans all definitions, never {@code null}.
	 */
	void addBeans(Bean[] beans) {
		for (Bean bean : beans) addBean(bean);
	}

	/**
	 * Gets the JNDI builder class
	 *
	 * @return the class of the JNDI builder, never {@code null}.
	 */
	Class<? extends JndiBuilder> getBuilder() {
		return builder;
	}

	/**
	 * Sets the JNDI builder class, never {@code null}.
	 *
	 * @param builder the class of JNDI builder.
	 */
	void setBuilder(Class<? extends JndiBuilder> builder) {
		Assert.notNull(builder, "The 'builder' argument cannot be null.");

		this.builder = builder;
	}

	// -- Getters

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


	/**
	 * TransactionManager definition. The {@link #name} sets the JNDI name of the queue.
	 */
	static class TransactionManagerDefinition {
		private final String name;

		TransactionManagerDefinition(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}
	}

	/**
	 * Bean definition. The {@link #name} sets the JNDI name of the queue; the {@link #type} sets the type of the bean
	 * to be created. The bean must have nullary constructor.
	 */
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

	/**
	 * JMS definition. The {@link #connectionFactoryName} sets the JNDI name of the JMS {@code ConnectionFactory}; the
	 * {@link #jmsQueues} and {@link #jmsTopics} defines the JMS queues and topics, respectively.
	 */
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

	/**
	 * JMS queue definition. The {@link #name} sets the JNDI name of the queue.
	 */
	static class JmsQueueDefinition {
		private final String name;

		JmsQueueDefinition(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}
	}

	/**
	 * JMS topic definition. The {@link #name} sets the JNDI name of the queue.
	 */
	static class JmsTopicDefinition {
		private final String name;

		JmsTopicDefinition(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}
	}

	/**
	 * Mail Session definition. The {@link #name} sets the JNDI name of the queue; the {@link #properties} sets
	 * the {@code javax.mail.Session} properties.
	 */
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

	/**
	 * The DataSource definition. The {@link #name} sets the JNDI name of the queue; the {@link #driverClass} specifies
	 * the JDBC driver; the {@link #url}, {@link #username}, {@link #password} sets the JDBC connection details.
	 */
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
