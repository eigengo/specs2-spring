package org.specs2.spring.web;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

/**
 * @author janm
 */
class SpringWebExtension {

	public void setup(Object specification) {
		final WebContextConfiguration webContextConfiguration = AnnotationUtils.findAnnotation(specification.getClass(), WebContextConfiguration.class);
		if (webContextConfiguration == null) return;

		final String[] webContextConfigurationResources = webContextConfiguration.value();
		String[] filesToFind = new String[webContextConfigurationResources.length + 1];
		for (int i = 0; i < webContextConfigurationResources.length; i++) {
			filesToFind[i + 1] = webContextConfigurationResources[i];
		}
		filesToFind[0] = "/WEB-INF/web.xml";
		final File root = new File(".");
		File webapp = findWebSource(root,
				new String[] {"src", "main", "webapp"},
				filesToFind);

		try {
			MockServletContext servletContext = new MockServletContext(webapp.getAbsolutePath(), new AbsoluteFilesystemResourceLoader());
			MockServletConfig servletConfig = new MockServletConfig(servletContext);
			servletContext.addInitParameter("contextConfigLocation",
					StringUtils.arrayToDelimitedString(webContextConfiguration.value(), "\n"));
			if (webContextConfigurationResources.length == 0) {
				throw new WebTestContextCreationException("You must specify servletContextConfiguration at this moment.");
			}
			servletConfig.addInitParameter("contextConfigLocation",
					StringUtils.arrayToDelimitedString(webContextConfigurationResources, "\n"));

			ContextLoaderListener listener = new ContextLoaderListener();
			listener.initWebApplicationContext(servletContext);

			final DispatcherServlet dispatcherServlet = new TracingDispatcherServlet();
			dispatcherServlet.init(servletConfig);
			DispatcherServletHolder.set(dispatcherServlet);
		} catch (Exception e) {
			throw new WebTestContextCreationException(e);
		}

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

}
