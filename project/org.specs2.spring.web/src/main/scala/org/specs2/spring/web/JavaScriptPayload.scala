package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse
import javax.servlet.http.HttpServletResponse

/**
 * @author janmachacek
 */

trait JavaScriptPayload extends PayloadRegistryAccess {
  register(parseJavascript _)

  def parseJavascript(response: MockHttpServletResponse) = {
    if (response.getStatus == HttpServletResponse.SC_OK && response.getContentType.startsWith("text/javascript"))
      None
    else
      Some(new JavaScriptWebObjectBody(response.getContentAsString))
  }

}

class JavaScriptWebObjectBody(payload: String) extends WebObjectBody[String, String](payload) {

  def <<[BB >: String, EE >: String](selector: String, value: Any) = this

  def >>[R](selector: String) = throw new RuntimeException("No JS evaluation yet")
}