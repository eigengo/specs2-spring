package org.specs2.spring

import annotation.Jndi
import org.springframework.test.context.ContextConfiguration

/**
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification {

  override def is : org.specs2.specification.Fragments = {
    val jndiEnvironmentSetter = new JndiEnvironmentSetter
    val testContextCreator = new TestContextCreator

    jndiEnvironmentSetter.prepareEnvironment(new EnvironmentExtractor().extract(this))
    testContextCreator.createAndAutowire(this)

    specFragments
  }

}