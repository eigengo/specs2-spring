package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.transaction.PlatformTransactionManager
import org.specs2.spring.TestTransactionDefinitionExtractor.TestTransactionDefinition
import org.specs2.specification.{Step, SpecStart, Example}

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
trait Specification extends org.specs2.mutable.SpecificationLike
  with SpecificationContext
  with SpecificationEnvironment {

  private[spring] val testContext = new TestContext
  private[spring] val environmentSetter = new JndiEnvironmentSetter

  private def setup() {
    environmentSetter.prepareEnvironment(new EnvironmentExtractor().extract(this))
    testContext.createAndAutowire(this)
  }

  override def is: org.specs2.specification.Fragments = {
    // setup the specification's transactional behaviour
    val ttd = new TestTransactionDefinitionExtractor().extract(this)
    val transformedFragments =
      if (ttd == TestTransactionDefinition.NOT_TRANSACTIONAL)
        // no transactions required
        fragments
      else {
        // transactions required, run each example body in a [separate] transaction
        fragments.map {
          f =>
            f match {
              case e: Example =>
                Example(e.desc, {
                  val transactionManager = testContext.getBean(ttd.getTransactionManagerName, classOf[PlatformTransactionManager])
                  val transactionStatus = transactionManager.getTransaction(ttd.getTransactionDefinition)
                  try {
                    val result = e.execute
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

      args(sequential = true) ^ Step(setup) ^ transformedFragments
  }

}