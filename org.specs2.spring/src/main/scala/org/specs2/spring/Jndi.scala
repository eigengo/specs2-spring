package org.specs2.spring


/**
 * @author janmachacek
 */
trait Jndi {
  this: SpecificationEnvironment =>
  
  def addJndiEntry(name: String, value: AnyRef) {
    environmentSetter.add(name, value)
  }

}
