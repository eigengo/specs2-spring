package org.specs2.web

import javax.servlet.http.HttpServletResponse
import org.springframework.web.servlet.ModelAndView
import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}

case class RR(request: MockHttpServletRequest, op: (MockHttpServletRequest) => MockHttpServletResponse)

/**
 * Abstract class that simplifies "standard" HTTP request processing
 *
 * @author janmachacek
 */
abstract class AbstractRR[B <: WebObjectBody](r: RR) {

  private def service(setup: MockHttpServletRequest => Unit) = {
    val request = r.request

    setup(request)

    val response = r.op(request)
    if (response.getRedirectedUrl != null) {
      response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
    }
    val unsafeMav = request.getAttribute(TracingDispatcherServlet.MODEL_AND_VIEW_KEY).asInstanceOf[ModelAndView]
    val mav = if (unsafeMav != null) Some(unsafeMav) else None

    val body = if (response.getStatus != HttpServletResponse.SC_OK) None else makeBody(response)
    new WebObject(request, response, mav, body)
  }

  def makeBody(response: MockHttpServletResponse): Option[B]

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