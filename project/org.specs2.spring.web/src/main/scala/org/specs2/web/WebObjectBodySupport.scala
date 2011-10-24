package org.specs2.web

import javax.servlet.http.HttpServletResponse
import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}

case class RR(request: MockHttpServletRequest,
         service: (MockHttpServletRequest) => MockHttpServletResponse)


/**
 * Abstract class that simplifies "standard" HTTP request processing
 *
 * @author janmachacek
 */
abstract class WebObjectBodySupport(r: RR) {

  type Body <: WebObjectBody

  private def service(setup: MockHttpServletRequest => Unit): Option[Body] = {
    val request = r.request

    setup(request)

    val response = r.service(request)
    if (response.getRedirectedUrl != null) {
      response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
    }

    val body = if (response.getStatus != HttpServletResponse.SC_OK) None else makeBody(response)

    body
  }

  def makeBody(response: MockHttpServletResponse): Option[Body]

  def apply(r: Requestable) = {
    service { request => r.request(request) }
  }

  def apply(url: String, params: Map[String, AnyRef]) = {
    service { request =>
      request.setRequestURI(url)
      params.foreach {
        e => request.setParameter(e._1, e._2.toString)
      }
    }
  }

  def apply(url: String) = {
    service { _.setRequestURI(url) }
  }

}