package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse

/**
 * @author janmachacek
 */

trait JavaScriptPayload extends PayloadRegistryAccess {
  register(parseJavascript _)

  def parseJavascript(response: MockHttpServletResponse) = {
    if (response.getContentType == "text/javascript")
      None
    else
      Some(new JavaScriptWebObjectBody(response.getContentAsString))
  }

}

class JavaScriptWebObjectBody(payload: String) extends WebObjectBody(payload) {

  def <<[R >: String](selector: String, value: String) = this

  def >>[R](selector: String) = throw new RuntimeException("No JS evaluation yet")

  def >>![R](selector: String) = throw new RuntimeException("No JS evaluation yet")
}