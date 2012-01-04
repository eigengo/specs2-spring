package org.specs2.spring.webexample.services;

import java.io.Serializable;
import java.util.List;

/**
 * @author janm
 */
public interface ManagementService {

	void save(Object o);

	<T> T get(Class<T> type, Serializable id);

	<T> List<T> findAll(Class<T> type);
}
