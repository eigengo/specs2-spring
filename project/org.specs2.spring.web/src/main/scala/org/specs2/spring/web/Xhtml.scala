package org.specs2.spring.web

import xml.{Node, XML}
import javax.servlet.http.HttpServletResponse
import org.springframework.web.servlet.ModelAndView
import org.springframework.mock.web.{MockHttpServletRequest, MockHttpServletResponse}
import Specification._

/**
 * Parses the XHTML responses
 *
 * @author janmachacek
 */
object Xhtml {

  def apply(r: R) = new Xhtml(r)

}

class Xhtml(val r: R) {

  private def service(setup: MockHttpServletRequest => Unit) = {
    val request = r.request

    setup(request)

    val response = r.op(request)
    if (response.getRedirectedUrl != null) {
      response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
    }
    val unsafeMav = request.getAttribute(TracingDispatcherServlet.MODEL_AND_VIEW_KEY).asInstanceOf[ModelAndView]
    val mav = if (unsafeMav != null) Some(unsafeMav) else None

    val body = if (response.getStatus != HttpServletResponse.SC_OK) None else Some(new XhtmlWebObjectBody(response.getContentAsString))
    new WebObject(request, response, mav, body)
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
    service { _ => }
  }

}

/**
 * Response body that assumes that the body is the XHTML of the returned page
 *
 * @param payload the XHTML string
 */
class XhtmlWebObjectBody(val body: String) extends WebObjectBody {
  private val dom = XML.loadString(body)

  def <<[BB >: String, EE >: String](selector: String, value: Any) = this

  def >>[R >: String](selector: String) = {
    require(selector != null)
    require("" != selector)

    def findElementBy(attribute: String, value: String) = {
      val input = dom \\ "_" find {
        n =>
          val attrs = n.attribute(attribute)
          if (attrs == None) false
          else attrs.get exists {
            _.text == value
          }
      }

      input
    }

    def findElementValue(element: Option[Node]) = {
      if (element == None) None
      val attributes = element.get.attribute("value")
      if (attributes == None) None

      Some(attributes.get.head.text)
    }

    selector.charAt(0) match {
      case '/' => Some((dom \\ selector).text)
      case '#' =>
        findElementValue(findElementBy("id", selector.substring(1)))
      case '.' =>
        findElementValue(findElementBy("class", selector.substring(1)))
      case _ =>
        findElementValue(findElementBy("class", selector))
    }

  }

}