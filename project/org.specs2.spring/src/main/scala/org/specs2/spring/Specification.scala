package org.specs2.spring

import org.springframework.test.context.ContextConfiguration

/**
 * @author janmachacek
 */

trait Specification extends org.specs2.mutable.Specification {

  override def is : org.specs2.specification.Fragments = {
    val jndiEnvironmentSetter = new JndiEnvironmentSetter
    val testContextCreator = new TestContextCreator

    val jndi = this.getClass.getAnnotation(classOf[Jndi])
    if (jndi != null) {
      jndiEnvironmentSetter.prepareEnvironment(jndi)
    }

    val contextConfiguration = this.getClass.getAnnotation(classOf[ContextConfiguration])
    if (contextConfiguration != null) {
      testContextCreator.createAndAutowire(contextConfiguration, this)
    }
    
    specFragments
  }

}