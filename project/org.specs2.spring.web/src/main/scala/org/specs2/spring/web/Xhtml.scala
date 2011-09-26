package org.specs2.spring.web

import xml.{Node, XML}
import Specification._
import org.springframework.mock.web.MockHttpServletResponse

/**
 * XHTML response companion object
 */
object Xhtml {

  def apply(r: RR) = new Xhtml(r)

}

/**
 * XHTML response object that
 *
 */
class Xhtml(r: RR) extends AbstractRR[XhtmlWebObjectBody](r) {

  def makeBody(response: MockHttpServletResponse) = 
    Some(new XhtmlWebObjectBody(response.getContentAsString))
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