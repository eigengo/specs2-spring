package org.specs2.spring.web;

import org.apache.jasper.servlet.JspServlet;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockRequestDispatcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author janm
 */
public class JspCapableMockHttpServletRequest extends MockHttpServletRequest {

	private final ServletConfig servletConfig;

	public JspCapableMockHttpServletRequest(String method, ServletConfig servletConfig) {
		super(method, "/");
		this.servletConfig = servletConfig;
		setRequestedSessionId("x");
		setRequestedSessionIdValid(true);
		setRequestedSessionIdFromCookie(true);
	}

	@Override
	public void setRequestURI(String requestURI) {
		super.setRequestURI(requestURI);
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return new RD(path, this.servletConfig);
	}

	static class RD extends MockRequestDispatcher {
		private final String url;
		private final ServletConfig servletConfig;
		/**
		 * Create a new MockRequestDispatcher for the given URL.
		 *
		 * @param url the URL to dispatch to.
		 */
		public RD(String url, ServletConfig servletConfig) {
			super(url);
			this.url = url;
			this.servletConfig = servletConfig;
		}

		@Override
		public void forward(ServletRequest request, ServletResponse response) {
			execute((HttpServletRequest) request, response);
		}

		@Override
		public void include(ServletRequest request, ServletResponse response) {
			execute((HttpServletRequest) request, response);
		}

		private void execute(final HttpServletRequest request, ServletResponse response) {
			JspServlet s = new JspServlet();
			try {
				s.init(this.servletConfig);
				fixupClassLoading();
				s.service(new HttpServletRequestWrapper((HttpServletRequest) request) {
					@Override
					public String getServletPath() {
						return url;
					}
				}, response);
				s.destroy();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private void fixupClassLoading() throws IOException {
			URLClassLoader contextClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
			if (contextClassLoader instanceof DelegateURLClassLoader) return;

			final Enumeration<URL> resources = contextClassLoader.findResources("META-INF");
			List<URL> urls = new ArrayList<URL>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				final String file = resource.getFile().replace("!/META-INF", "").replace("file:", "");
				final URL fileUrl = new File(file).toURI().toURL();
				urls.add(fileUrl);
			}
			URLClassLoader delegateClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), contextClassLoader);

			Thread.currentThread().setContextClassLoader(delegateClassLoader);
		}
	}

	static class DelegateURLClassLoader extends URLClassLoader {

		public DelegateURLClassLoader(URL[] urls, ClassLoader classLoader) {
			super(urls, classLoader);
		}
	}

}
