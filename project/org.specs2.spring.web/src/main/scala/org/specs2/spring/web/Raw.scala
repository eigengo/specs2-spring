package org.specs2.spring.web

import Specification._
import org.springframework.mock.web.MockHttpServletResponse

/**
 * Raw companion object for no-operation parser
 */
object Raw {

  def apply(r: RR) = new Raw(r)

}

/**
 * Raw implementation of the HTTP response processing
 */
class Raw(r: RR) extends AbstractRR[RawWebObjectBody](r) {
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

  def bodyString = new String(body)

  override def toString = bodyString

}