package org.specs2.spring


/**
 * @author janmachacek
 */
trait SettableEnvironment {
  this: SpecificationEnvironment =>
  
  def addJndiEntry(name: String, value: AnyRef) {
    environmentSetter.add(name, value)
  }

}
