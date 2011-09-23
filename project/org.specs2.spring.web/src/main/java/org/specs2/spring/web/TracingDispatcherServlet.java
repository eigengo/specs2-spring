package org.specs2.spring.web;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author janm
 */
class TracingDispatcherServlet extends DispatcherServlet {
	private static final long serialVersionUID = 6724861164448416351L;
	static final String MODEL_AND_VIEW_KEY = "__**MAV**__";
	private ModelAndViewRecordingInterceptor interceptor = new ModelAndViewRecordingInterceptor();

	@Override
	protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		final HandlerExecutionChain handler = super.getHandler(request);
		if (handler == null) return null;
		
		boolean interceptorFound = false;
		for (HandlerInterceptor interceptor : handler.getInterceptors()) {
			if (interceptor == this.interceptor) {
				interceptorFound = true;
			}
		}
		if (!interceptorFound) handler.addInterceptor(this.interceptor);
		return handler;
	}

	@Override
	protected void onRefresh(ApplicationContext context) {
		super.onRefresh(context);
		//test was publishing the underlying web app context instead of the full application context, there may be a better way of doing this.
		getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
	}

	private static class ModelAndViewRecordingInterceptor implements HandlerInterceptor {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			return true;
		}

		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
			request.setAttribute(MODEL_AND_VIEW_KEY, modelAndView);
		}

		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

		}
	}
}
