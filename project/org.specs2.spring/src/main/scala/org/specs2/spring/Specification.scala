package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.hibernate3.HibernateTemplate
import org.specs2.specification.Example
import org.springframework.transaction.PlatformTransactionManager
import org.specs2.spring.TestTransactionDefinitionExtractor.TestTransactionDefinition

/**
 * Mutable Specification that sets up the JNDI environment and autowires the fields / setters of its subclasses.
 *
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification {
  private val testContext = new TestContext

  /**
   * Obtains a single bean of type JdbcTemplate; throws exception if the test context does not define
   * exactly one such bean.
   *
   * @return the JdbcTemplate bean; never {@code null}.
   */
  private[spring] def getJdbcTemplate: JdbcTemplate = {
    this.testContext.getBean(classOf[JdbcTemplate])
  }

  /**
   * Obtains a single bean of type HibernateTemplate; throws exception if the test context does not define
   * exactly one such bean.
   *
   * @return the HibernateTemplate bean; never {@code null}.
   */
  private[spring] def getHibernateTemplate: HibernateTemplate = {
    this.testContext.getBean(classOf[HibernateTemplate])
  }

  override def is: org.specs2.specification.Fragments = {
    // setup the specification's environment
    new JndiEnvironmentSetter().prepareEnvironment(new EnvironmentExtractor().extract(this))
    this.testContext.createAndAutowire(this)

    // setup the specification's transactional behaviour
    val ttd = new TestTransactionDefinitionExtractor().extract(this)
    if (ttd == TestTransactionDefinition.NOT_TRANSACTIONAL)
      // no transactions required
      this.specFragments
    else {
      // transactions required, run each example body in a [separate] transaction
      val transactionManager = this.testContext.getBean(classOf[PlatformTransactionManager])

      this.specFragments.map {
        f =>
          f match {
            case Example(desc, body) =>
              Example.apply(desc, {
                val transactionStatus = transactionManager.getTransaction(ttd.getTransactionDefinition)
                try {
                  val result = body()
                  if (!ttd.isDefaultRollback) transactionManager.commit(transactionStatus)
                  result
                } finally {
                  if (ttd.isDefaultRollback) transactionManager.rollback(transactionStatus)
                }
              })
            case _ => f
          }
      }
    }
  }

}