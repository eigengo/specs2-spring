package org.specs2.spring.web

import collection.mutable.MutableList
import org.springframework.mock.web.MockHttpServletResponse

/**
 * @author janmachacek
 */
private[web] object Payloads {
  private[web] type PayloadFunction = (MockHttpServletResponse) => Option[WebObjectBody[_]]
}

trait PayloadRegistry extends PayloadRegistryAccess {
  import Payloads.PayloadFunction
  val payloadFunctions = new MutableList[PayloadFunction]

  def register(f: PayloadFunction) {
    this.payloadFunctions += f
  }

}

trait PayloadRegistryAccess {
  import Payloads.PayloadFunction
  def register(f: PayloadFunction)
}