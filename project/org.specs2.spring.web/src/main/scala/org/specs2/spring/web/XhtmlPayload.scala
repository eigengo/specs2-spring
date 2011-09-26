package org.specs2.spring.web

import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}

/**
 * @author janmachacek
 */

trait XhtmlPayload extends PayloadRegistryAccess {
  register(parseXhtml _)

  def parseXhtml(request: MockHttpServletResponse) = {
    if (request.getContentType startsWith "text/html;charset=UTF-8")
      Some(new XhtmlWebObjectBody(request.getContentAsString))
    else
      None
  }

}

class XhtmlWebObjectBody(payload: String) extends WebObjectBody(payload) {

  def <<[R >: String](selector: String, value: String) = this

  def >>[R](selector: String) = None

  def >>![R](selector: String) = >>(selector).get
}