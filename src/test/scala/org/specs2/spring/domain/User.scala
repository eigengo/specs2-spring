package org.specs2.spring.domain

import reflect.BeanProperty
import javax.persistence.{Version, Entity, Id, GeneratedValue}

/**
 * @author janmachacek
 */

@Entity
case class User() {
  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _
  @Version
  @BeanProperty
  var version: Int = _
  @BeanProperty
  var username: String = _
  @BeanProperty
  var firstName: String = _
  @BeanProperty
  var lastName: String = _

}
