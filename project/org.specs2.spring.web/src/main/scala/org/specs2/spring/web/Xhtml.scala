package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse
import xml.{Node, XML}
import javax.servlet.http.HttpServletResponse
import org.springframework.web.servlet.ModelAndView

/**
 * Parses the XHTML responses
 *
 * @author janmachacek
 */
object Xhtml {
  import Specification._

  def apply(rr: RR) = {
      if (rr.response.getRedirectedUrl != null) {
        rr.response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
      }

      val unsafeMav = rr.request.getAttribute(TracingDispatcherServlet.MODEL_AND_VIEW_KEY).asInstanceOf[ModelAndView]
      val mav = if (unsafeMav != null) Some(unsafeMav) else None

      new WebObject(rr.request, rr.response, mav, Some(new XhtmlWebObjectBody(rr.response.getContentAsString)))
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
      val input = dom \\ "_" find { n =>
        val attrs = n.attribute(attribute)
        if (attrs == None) false
        else attrs.get exists {_.text == value}
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