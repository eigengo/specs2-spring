package org.spockframework.springintegration.web;

import org.spockframework.runtime.extension.AbstractMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.ErrorInfo;

/**
 * @author janm
 */
class SpringWebInterceptor extends AbstractMethodInterceptor {
	private final SpringWebTestContextManager manager;

	private Throwable exception;
	private boolean beforeTestMethodInvoked = false;

	public SpringWebInterceptor(SpringWebTestContextManager manager) {
		this.manager = manager;
	}

	@Override
	public void interceptSetupSpecMethod(IMethodInvocation invocation) throws Throwable {
		invocation.proceed();
	}

	@Override
	public void interceptSetupMethod(IMethodInvocation invocation) throws Throwable {
		this.manager.prepareTestInstance(invocation.getTarget());
		this.exception = null;
		this.beforeTestMethodInvoked = true;
		this.manager.beforeTestMethod(invocation.getTarget(), invocation.getFeature().getFeatureMethod().getReflection());
		invocation.proceed();
	}

	@Override
	public void interceptCleanupMethod(IMethodInvocation invocation) throws Throwable {
		if (!this.beforeTestMethodInvoked) {
			invocation.proceed();
			return;
		}
		this.beforeTestMethodInvoked = false;

		Throwable cleanupEx = null;
		try {
			invocation.proceed();
		} catch (Throwable t) {
			cleanupEx = t;
			if (this.exception == null) this.exception = t;
		}

		Throwable afterTestMethodEx = null;
		try {
			this.manager.afterTestMethod(invocation.getTarget(), invocation.getFeature().getFeatureMethod().getReflection(), this.exception);
		} catch (Throwable t) {
			afterTestMethodEx = t;
		}

		if (cleanupEx != null) throw cleanupEx;
		if (afterTestMethodEx != null) throw afterTestMethodEx;
	}

	@Override
	public void interceptCleanupSpecMethod(IMethodInvocation invocation) throws Throwable {
		Throwable cleanupSpecEx = null;
		try {
			invocation.proceed();
		} catch (Throwable t) {
			cleanupSpecEx = t;
		}

		Throwable afterTestClassEx = null;
		try {
			this.manager.afterTestClass();
		} catch (Throwable t) {
			afterTestClassEx = t;
		}

		if (cleanupSpecEx != null) throw cleanupSpecEx;
		if (afterTestClassEx != null) throw afterTestClassEx;
	}

	public void error(ErrorInfo error) {
		if (this.exception == null)  this.exception = error.getException();
	}
}
