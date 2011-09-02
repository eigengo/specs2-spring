package org.specs2.spring

/**
 * @author janmachacek
 */

trait BeanTables {

  implicit def toBeans(a: scala.Predef.String) = {
    new BeanRow()
  }

  case class BeanRow() {
    def |>[T](checker: T => Unit)(implicit m: ClassManifest[T]) {
      val t = m.erasure.newInstance()
      println(t)
    }
  }
}