package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse
import javax.servlet.http.HttpServletResponse

/**
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

class MovedWebObjectBody(private val to: String) extends WebObjectBody[String, String](to) {

  def >>[R >: String](selector: String) = throw new RuntimeException("No attributes of moved.")

  def >>![R >: String](selector: String) = throw new RuntimeException("No attributes of moved.")

  def <<[BB >: Null, EE >: Null](selector: String, value: String) = throw new RuntimeException("No attributes of moved.")
}
