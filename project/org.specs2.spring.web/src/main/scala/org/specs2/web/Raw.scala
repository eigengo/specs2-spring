package org.specs2.web

import org.springframework.mock.web.MockHttpServletResponse

/**
 * Raw companion object for no-operation parser
 */
object Raw {

  /**
   * Processes the input to return the {{Raw}} instance that will deal with
   * the request.
   *
   * @param r the RR object representing the request
   * @return the {{Raw}} instance that will deal with the request
   */
  def apply(r: RR) = new Raw(r)

}

/**
 * Raw implementation of the HTTP response processing
 */
class Raw(r: RR) extends WebObjectBodySupport(r) {
  type Body = RawWebObjectBody

  def makeBody(response: MockHttpServletResponse) =
    Some(new RawWebObjectBody(response.getContentAsByteArray))
}

/**
 * Raw implementation of the HTTP web object body, with the payload
 * as byte array
 *
 * @param body the body of the request
 */
class RawWebObjectBody(val body: Array[Byte]) extends WebObjectBody {

  /**
   * Returns the response as String in the encoding of the VM
   *
   * @return the String representation of the content
   */
  def bodyString = new String(body)

  override def toString = bodyString

}