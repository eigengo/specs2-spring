package org.specs2.spring.web

import collection.mutable.MutableList
import org.springframework.mock.web.MockHttpServletResponse

/**
 * Defines the common elements to be used by the payload processing code.
 *
 * @author janmachacek
 */
private[web] object Payloads {
  /**
   * Function that takes the {{MockHttpServletResponse}} and produces, if possible,
   * a {{WebObjectBody}} implementation for that response.
   */
  private[web] type PayloadFunction = (MockHttpServletResponse) => Option[WebObjectBody[_, _]]
}

/**
 * Mixin that contains the code that allows the different payload processing functions to register themselves.
 */
trait PayloadRegistry extends PayloadRegistryAccess {
  import Payloads.PayloadFunction
  val payloadFunctions = new MutableList[PayloadFunction]

  /**
   * Registers the function that processes the payload.
   *
   * @param f the payload processing function
   */
  def register(f: PayloadFunction) {
    this.payloadFunctions += f
  }

}

/**
 * Mixin that allows the different payload processing functions to register themselves.
 */
trait PayloadRegistryAccess {
  import Payloads.PayloadFunction

  /**
   * Registers the function that processes the payload.
   *
   * @param f the payload processing function
   */
  def register(f: PayloadFunction)
}