package org.specs2.spring.web

/**
 * @author janm
 */
trait Specification extends org.specs2.spring.Specification {

  override def is: org.specs2.specification.Fragments = {
    super.is
  }
  /*
	public void visitSpec(SpecInfo spec) {
		final WebContextConfiguration webContextConfiguration = AnnotationUtils.findAnnotation(spec.getReflection(), WebContextConfiguration.class);
		if (webContextConfiguration == null) return;
		ContextConfiguration contextConfiguration = AnnotationUtils.findAnnotation(spec.getReflection(), ContextConfiguration.class);
		if (contextConfiguration.loader() != ExistingApplicationContextLoader.class ||
				contextConfiguration.value().length > 0 ||
				contextConfiguration.locations().length > 0) {
			throw new WebTestContextCreationException("Do not annotate your web test cases with @ContextConfiguration. Extend WebSpecification instead.");
		}

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
					StringUtils.arrayToDelimitedString(webContextConfiguration.contextConfiguration().value(), "\n"));
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
	*/

}

/*
import org.specs2.spring.web.Specification

@WebTest
class UserControllerTest extends Specification {
	@Autowired var UserService userService

	"create, view and edit"() in {
		val username = "janm"
		post("/users.html", Map("username" -> username, "fullName" -> "Jan Machacek"))
		val wo = get("/users/1.html")
		wo << ("#fullName", "Jan")
		post(wo)

		val user = this.userService.getByUsername(username)
		user.getFullName must_==("Jan")
	}

	"posting with validation"() in {
		val username = "janm"
		val fullName = "Jan Machacek"

		val wp = post("/users.html", Map("username" -> "x", "fullName" -> "")
		wp.hasFieldErrorFor("fullName") must_== (true)
		wp.hasFieldErrorFor("username") must_== (true)

		// correct the errors, submit again
		wp << ("#fullName", fullName)
		wp << ("#username", username)
		post(wp)

		// because of the second valid post, we should now have the user in the DB
		val user = this.userService.getByUsername(username)
		user.getFullName must_== (fullName)
	}

}

@Transactional
@TransactionConfiguration(defaultRollback = true)
@WebContextConfiguration(
	value = "/WEB-INF/sw-servlet.xml",
	contextConfiguration = @ContextConfiguration("classpath*:/META-INF/spring/module-context.xml")
)
@Jndi(
		dataSources = @DataSource(name = "java:comp/env/jdbc/test",
				driverClass = JDBCDriver.class, url = "jdbc:hsqldb:mem:test"),
		beans = @Bean(name = "java:comp/env/bean/hibernateProperties", type = HibernateProperties.class)
)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WebTest {
}

*/