package org.specs2.spring.web;

import org.specs2.spring.TestTransactionDefinitionExtractor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

/**
 * @author janm
 */
class TestContext {
    private DispatcherServlet dispatcherServlet;
    private PlatformTransactionManager platformTransactionManager;
    private TestTransactionDefinitionExtractor.TestTransactionDefinition testTransactionDefinition;

    public void setup(Object specification) {
        final WebContextConfiguration webContextConfiguration = AnnotationUtils.findAnnotation(specification.getClass(), WebContextConfiguration.class);
        if (webContextConfiguration == null) return;

        final String[] webContextConfigurationResources = webContextConfiguration.webContextLocations();
        String[] filesToFind = new String[webContextConfigurationResources.length + 1];
        for (int i = 0; i < webContextConfigurationResources.length; i++) {
            filesToFind[i + 1] = webContextConfigurationResources[i];
        }
        filesToFind[0] = "/WEB-INF/web.xml";
        final File root = new File(".");
        File webapp = findWebSource(root,
                new String[]{"src", "main", "webapp"},
                filesToFind);

        try {
            MockServletContext servletContext = new MockServletContext(webapp.getAbsolutePath(), new AbsoluteFilesystemResourceLoader());
            MockServletConfig servletConfig = new MockServletConfig(servletContext);
            servletContext.addInitParameter("contextConfigLocation",
                    StringUtils.arrayToDelimitedString(webContextConfiguration.contextLocations(), "\n"));
            if (webContextConfigurationResources.length == 0) {
                // in the future, I would like to detect the settings from web.xml
                throw new WebTestContextCreationException("You must specify servletContextConfiguration at this moment.");
            }
            servletConfig.addInitParameter("contextConfigLocation",
                    StringUtils.arrayToDelimitedString(webContextConfigurationResources, "\n"));

            ContextLoaderListener listener = new ContextLoaderListener();
            listener.initWebApplicationContext(servletContext);

            this.dispatcherServlet = new TracingDispatcherServlet();
            this.dispatcherServlet.init(servletConfig);

            autowire(this.dispatcherServlet.getWebApplicationContext(), specification);
        } catch (Exception e) {
            throw new WebTestContextCreationException(e);
        }

    }

    private void autowire(ApplicationContext applicationContext, Object specification) {
        if (applicationContext == null) return;
        applicationContext.getAutowireCapableBeanFactory().autowireBean(specification);
        autowire(applicationContext.getParent(), specification);
    }

    DispatcherServlet getDispatcherServlet() {
        return this.dispatcherServlet;
    }

    <T> T getBean(Class<T> type) {
        return this.dispatcherServlet.getWebApplicationContext().getBean(type);
    }

    private File findWebSource(File root, String[] path, String[] filesToFind) {
        File webInf = findFile(root, path);
        if (webInf != null) return webInf;
        for (File directory : root.listFiles()) {
            if (!directory.isDirectory()) continue;
            webInf = findWebSource(directory, path, filesToFind);
            if (webInf != null) {
                boolean foundAllFiles = true;
                for (int i = 0; i < filesToFind.length; i++) {
                    if (!(new File(webInf, filesToFind[i])).exists()) {
                        foundAllFiles = false;
                        break;
                    }
                }

                if (foundAllFiles) return webInf;
            }
        }
        return null;
    }

    private File findFile(File root, String... path) {
        File subpath = root;
        for (int i = 0; i < path.length; i++) {
            subpath = new File(subpath, path[i]);
            if (!subpath.exists()) return null;
        }
        return subpath;
    }

    PlatformTransactionManager getPlatformTransactionManager() {
        return platformTransactionManager;
    }

    void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    TestTransactionDefinitionExtractor.TestTransactionDefinition getTestTransactionDefinition() {
        return testTransactionDefinition;
    }

    void setTestTransactionDefinition(TestTransactionDefinitionExtractor.TestTransactionDefinition testTransactionDefinition) {
        this.testTransactionDefinition = testTransactionDefinition;
    }
}
