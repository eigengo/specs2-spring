package org.specs2.spring;

import org.specs2.spring.annotation.Property;
import org.specs2.spring.annotation.SystemEnvironment;
import org.specs2.spring.annotation.SystemProperties;
import org.specs2.spring.annotation.UseProfile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates Spring ApplicationContext for the test
 *
 * @author janmachacek
 */
class TestContext {

	private GenericXmlApplicationContext context;

	/**
	 * Creates the {@link ApplicationContext} and autowire the fields / setters test object.
	 *
	 * @param specification the specification object to set up.
	 */
	void createAndAutowire(Object specification) {
		final ContextConfiguration contextConfiguration = AnnotationUtils.findAnnotation(specification.getClass(), ContextConfiguration.class);
		if (contextConfiguration == null) return;
		
		this.context = new GenericXmlApplicationContext();
		this.context.setEnvironment(setupTestEnvironment(specification));
		this.context.load(contextConfiguration.value());
		this.context.refresh();
		this.context.getAutowireCapableBeanFactory().autowireBean(specification);
	}

	private ConfigurableEnvironment setupTestEnvironment(Object specification) {
		TestEnvironment environment = new TestEnvironment();
		final UseProfile usePro = AnnotationUtils.findAnnotation(specification.getClass(), UseProfile.class);
		if (usePro != null && usePro.value().length > 0) environment.setActiveProfiles(usePro.value());

		final SystemEnvironment systemEnvironment = AnnotationUtils.findAnnotation(specification.getClass(), SystemEnvironment.class);
		if (systemEnvironment != null) {
			environment.setSystemEnvironment(systemEnvironment.clear(), systemEnvironment.overwrite(), 
					systemEnvironment.nullValue(), systemEnvironment.value(), systemEnvironment.properties());
		}
		final SystemProperties systemProperties = AnnotationUtils.findAnnotation(specification.getClass(), SystemProperties.class);
		if (systemProperties != null) {
			environment.setSystemProperties(systemProperties.clear(), systemProperties.overwrite(),
					systemProperties.nullValue(), systemProperties.value(), systemProperties.properties());
		}

		return environment;
	}

	<T> T getBean(Class<T> beanType) {
		return this.context.getBean(beanType);
	}
	
	static final class TestEnvironment extends StandardEnvironment {
		private Properties systemProperties = null;
		private Properties systemEnvironment = null;
		
		void setSystemProperties(boolean clear, boolean override, String nullValue, String[] value, Property[] properties) {
			
		}
		
		void setSystemEnvironment(boolean clear, boolean override, String nullValue, String[] value, Property[] properties) {
			
		}
		
		private void set(Properties target, boolean clear, boolean override, String nullValue, String[] values, Property[] properties) {
			
		}
		
		@Override
		public Map<String, Object> getSystemProperties() {
			if (this.systemProperties == null) return super.getSystemProperties();
			return this.systemProperties.getProperties();
		}

		@Override
		public Map<String, Object> getSystemEnvironment() {
			if (this.systemEnvironment == null) return super.getSystemEnvironment();
			return this.systemEnvironment.getProperties();
		}
	}

	static final class Properties {
		private final boolean overwrite;
		private final Map<String, Object> environment;
		private final String nullValue;

		Properties(boolean overwrite, Map<String, Object> environment, String nullValue) {
			this.overwrite = overwrite;
			this.environment = environment;
			this.nullValue = nullValue;
		}
		
		void addProperties(String[] properties) {
			
		}

		void addProperties(Property[] properties) {

		}

		public Map<String,Object> getProperties() {
			return new HashMap<String, Object>();
		}
	}
}
