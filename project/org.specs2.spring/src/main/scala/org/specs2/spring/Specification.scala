package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate

/**
 * Mutable Specification that sets up the JNDI environment and autowires the fields / setters of its subclasses.
 *
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification {
  private val testContextCreator = new TestContextCreator
  private val jndiEnvironmentSetter = new JndiEnvironmentSetter

  private[spring] def getJdbcTemplate: JdbcTemplate = {
    null
  }

  override def is : org.specs2.specification.Fragments = {
    this.jndiEnvironmentSetter.prepareEnvironment(new EnvironmentExtractor().extract(this))
    this.testContextCreator.createAndAutowire(this)

    this.specFragments
  }

}