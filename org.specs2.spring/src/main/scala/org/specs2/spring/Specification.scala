package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.transaction.PlatformTransactionManager
import org.specs2.spring.TestTransactionDefinitionExtractor.TestTransactionDefinition
import org.specs2.specification.Example

/**
 * Gives access to the Sprnig context for the specification
 */
trait SpecificationContext {
  
  private[spring] def testContext: TestContext
  
}

/**
 * Gives access to the JNDI environment for the specification
 */
trait SpecificationEnvironment {

  private[spring] def environmentSetter: JndiEnvironmentSetter
  
}

/**
 * Mutable Specification that sets up the JNDI environment and autowires the fields / setters of its subclasses.
 *
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification
  with SpecificationContext
  with SpecificationEnvironment {

  private[spring] val testContext = new TestContext
  private[spring] val environmentSetter = new JndiEnvironmentSetter

  /**
   * Obtains a single bean of type JdbcTemplate; throws exception if the test context does not define
   * exactly one such bean.
   *
   * @return the JdbcTemplate bean; never ``null``.
   */
  protected[spring] def getJdbcTemplate: JdbcTemplate = {
    testContext.getBean(classOf[JdbcTemplate])
  }

  /**
   * Obtains a single bean of type HibernateTemplate; throws exception if the test context does not define
   * exactly one such bean.
   *
   * @return the HibernateTemplate bean; never ``null``.
   */
  protected[spring] def getHibernateTemplate: HibernateTemplate = {
    testContext.getBean(classOf[HibernateTemplate])
  }
  
  override def is: org.specs2.specification.Fragments = {
    // setup the specification's environment
    environmentSetter.prepareEnvironment(new EnvironmentExtractor().extract(this))
    testContext.createAndAutowire(this)

    // setup the specification's transactional behaviour
    val ttd = new TestTransactionDefinitionExtractor().extract(this)
    if (ttd == TestTransactionDefinition.NOT_TRANSACTIONAL)
      // no transactions required
      specFragments
    else {
      // transactions required, run each example body in a [separate] transaction
      val transactionManager = testContext.getBean(ttd.getTransactionManagerName, classOf[PlatformTransactionManager])

      specFragments.map {
        f =>
          f match {
            case Example(desc, body) =>
              Example(desc, {
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