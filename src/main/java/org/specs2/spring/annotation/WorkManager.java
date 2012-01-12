package org.specs2.spring.annotation;

/**
 * Specifies the JNDI-bound WorkManager, either the
 * <code>commonj.work.WorkManager</code> (when type is {@link org.specs2.spring.annotation.WorkManager.Kind#CommonJ}) or
 * <code>javax.spi.resource.work.WorkManager</code> (when type is {@link org.specs2.spring.annotation.WorkManager.Kind#Javax}).<br/>
 * The specified WorkManager will be bound at the given {@link #name()} and its thread pool will be configured to have
 * at least {@link #minimumThreads()} and at most {@link #maximumThreads()}.<br/>
 * The {@link #minimumThreads()} should be smaller than {@link #maximumThreads()}.
 *
 * @author janmachacek
 */
public @interface WorkManager {

	/**
	 * The type of WorkManager to create
	 */
	public static enum Kind {
		/**
		 * Create the <code>commonj.work.WorkManager</code>
		 */
		CommonJ,
		/**
		 * Create the <code>javax.spi.resource.work.WorkManager</code>
		 */
		Javax
	}

	/**
	 * The JNDI name for the WorkManager
	 *
	 * @return the name, typically "java:comp/env/work/WorkManager"
	 */
	String name();

	/**
	 * The kind of the WorkManager to create
	 *
	 * @return the type
	 */
	Kind kind();

	/**
	 * The minimum number of threads for the WorkManager
	 *
	 * @return the number of threads; always > 0.
	 */
	int minimumThreads() default 2;

	/**
	 * The maximum number of threads for the WorkManager.
	 *
	 * @return the number of threads; always > 1 and <= {@link #minimumThreads()}
	 */
	int maximumThreads() default 4;

}
