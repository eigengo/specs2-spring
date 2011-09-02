package org.specs2.spring

/**
 * Mutable Specification that sets up the JNDI environment and autowires the fields / setters of its subclasses.
 *
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