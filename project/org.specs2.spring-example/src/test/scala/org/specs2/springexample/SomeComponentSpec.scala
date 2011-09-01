package org.specs2.springexample

import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.transaction.TransactionConfiguration
import org.specs2.spring.{Specification}
import org.hsqldb.jdbc.JDBCDriver
import org.specs2.spring.annotation.{WorkManager, TransactionManager, DataSource}

/**
 * @author janmachacek
 */
@IntegrationTest
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