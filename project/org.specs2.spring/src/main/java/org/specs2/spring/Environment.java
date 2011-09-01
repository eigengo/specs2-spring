package org.specs2.spring;

import org.specs2.spring.annotation.DataSource;
import org.specs2.spring.annotation.TransactionManager;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

/**
 * @author janmachacek
 */
class Environment {
	private List<DataSourceDefinition> dataSources = new ArrayList<DataSourceDefinition>();
	private List<TransactionManagerDefinition> transactionManagers = new ArrayList<TransactionManagerDefinition>();

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

	List<DataSourceDefinition> getDataSources() {
		return dataSources;
	}

	List<TransactionManagerDefinition> getTransactionManagers() {
		return transactionManagers;
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
