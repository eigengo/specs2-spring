package org.specs2.springexample

import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.transaction.TransactionConfiguration
import org.specs2.spring.annotation.{Jndi, DataSource}
import org.specs2.spring.{Specification}
import org.hsqldb.jdbc.JDBCDriver

/**
 * @author janmachacek
 */
/*@Jndi(
		dataSources = @DataSource(name = "java:comp/env/jdbc/test",
				driverClass = JDBCDriver.class, url = "jdbc:hsqldb:mem:test"),
		mailSessions = @MailSession(name = "java:comp/env/mail/foo"),
		transactionManager = @TransactionManager(name = "java:comp/TransactionManager"),
		jms = @Jms(
				connectionFactoryName = "java:comp/env/jms/connectionFactory",
				queues = @Queue(name = "java:comp/env/jms/queue")
		)
)
*/
@DataSource(name = "java:comp/env/jdbc/test", driverClass = classOf[JDBCDriver], url = "jdbc:hsqldb:mem:test")
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(Array("classpath*:/META-INF/spring/module-context.xml"))
class SomeComponentSpec extends Specification {
  @Autowired var someComponent: SomeComponent = _
  @Autowired var hibernateTemplate: HibernateTemplate = _

  "Some such" in {
    "generate 10 users " ! generate(100)
  }

  def generate(count: Int) = {
    this.someComponent.generate(count)
    this.hibernateTemplate.loadAll(classOf[Rider]) must have size (count)
  }

}