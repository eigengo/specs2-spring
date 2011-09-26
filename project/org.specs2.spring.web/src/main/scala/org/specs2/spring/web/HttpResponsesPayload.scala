package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse
import javax.servlet.http.HttpServletResponse

/**
 * Parses the non-OK HTTP response codes
 *
 * @author janmachacek
 */
class HttpResponsesPayload {

  def parseHttpResponses(response: MockHttpServletResponse) = {
    response.getStatus match {
      case HttpServletResponse.SC_MOVED_TEMPORARILY => Some(new MovedWebObjectBody(response.getRedirectedUrl))
      // add other cases here

      case _ => None
    }
  }

}

/**
 * WebObjectBody representing the HTTP status codes 3xx: moved temporarily and moved permanently. The HTTP 3xx status
 * does not carry any attributes; the {{payload}} is the new URL.
 *
 * @param to the URL the object moved to.
 */
class MovedWebObjectBody(private val to: String) extends WebObjectBody[String, String](to) {

  def >>[R >: String](selector: String) = throw new RuntimeException("No attributes of moved.")

  def >>![R >: String](selector: String) = throw new RuntimeException("No attributes of moved.")

  def <<[BB >: Null, EE >: Null](selector: String, value: String) = throw new RuntimeException("No attributes of moved.")
}
