package org.specs2.spring.web

import collection.mutable.MutableList

/**
 * @author janmachacek
 */
private[web] object Payloads {
  private[web] type PayloadFunction = (String, Array[Byte]) => Option[String]
}

trait PayloadRegistry extends PayloadRegistryAccess {
  import Payloads.PayloadFunction
  val payloadFunctions = new MutableList[PayloadFunction]

  def x(f: PayloadFunction) {
    this.payloadFunctions += f
  }

}

trait PayloadRegistryAccess {
  import Payloads.PayloadFunction
  def x(f: PayloadFunction)
}