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

    <T> T getBean(String beanName, Class<T> beanType) {
        return this.context.getBean(beanName, beanType);
    }

    /**
     * Holds the environment for the test
     */
    static final class TestEnvironment extends StandardEnvironment {
        private Properties systemProperties = null;
        private Properties systemEnvironment = null;

        void setSystemProperties(boolean clear, boolean overwrite, String nullValue, String[] value, Property[] properties) {
            final Map<String, Object> props = new HashMap<String, Object>();
            if (!clear) props.putAll(super.getSystemProperties());

            this.systemProperties = new Properties(overwrite, props, nullValue);
            this.systemProperties.addProperties(properties);
            this.systemProperties.addProperties(value);
        }

        void setSystemEnvironment(boolean clear, boolean overwrite, String nullValue, String[] value, Property[] properties) {
            final Map<String, Object> props = new HashMap<String, Object>();
            if (!clear) props.putAll(super.getSystemEnvironment());

            this.systemEnvironment = new Properties(overwrite, props, nullValue);
            this.systemEnvironment.addProperties(properties);
            this.systemEnvironment.addProperties(value);
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

    /**
     * Holds the properties that can be added or overwritten
     */
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
            for (String property : properties) {
                int i = property.indexOf("=");
                if (i != -1) {
                    String name = property.substring(0, i);
                    String value = property.substring(i);

                    addProperty(name, value);
                }
            }
        }

        void addProperties(Property[] properties) {
            for (Property property : properties) {
                addProperty(property.name(), property.value());
            }
        }

        private void addProperty(String name, String value) {
            if (!this.overwrite) {
                if (this.environment.containsKey(name)) return;
            }
            Object realValue;
            if (this.nullValue.equals(value)) {
                realValue = null;
            } else {
                realValue = value;
            }
            this.environment.put(name, realValue);
        }

        Map<String, Object> getProperties() {
            return this.environment;
        }
    }
}
