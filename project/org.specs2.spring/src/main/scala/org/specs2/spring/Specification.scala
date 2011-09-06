package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.hibernate3.HibernateTemplate
import org.specs2.specification.Example
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.specs2.execute.Success

/**
 * Mutable Specification that sets up the JNDI environment and autowires the fields / setters of its subclasses.
 *
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification {
  private val testContext = new TestContext

  private[spring] def getJdbcTemplate: JdbcTemplate = {
    this.testContext.getBean(classOf[JdbcTemplate])
  }

  private[spring] def getHibernateTemplate: HibernateTemplate = {
    this.testContext.getBean(classOf[HibernateTemplate])
  }

  override def is : org.specs2.specification.Fragments = {
    new JndiEnvironmentSetter().prepareEnvironment(new EnvironmentExtractor().extract(this))
    this.testContext.createAndAutowire(this)

    // decide whether we need to be transactional or not...
    // suppose we do for now.
    val transactionManager = this.testContext.getBean(classOf[PlatformTransactionManager])

    this.specFragments.map { f =>
      f match {
        case Example(desc, body) =>
          Example.apply(desc, {
            val transactionDefinition = new DefaultTransactionDefinition()
            val transactionStatus = transactionManager.getTransaction(transactionDefinition)
            try {
              body()
            } finally {
              transactionManager.rollback(transactionStatus)
            }
          })
        case _ => f
      }
    }
  }

}