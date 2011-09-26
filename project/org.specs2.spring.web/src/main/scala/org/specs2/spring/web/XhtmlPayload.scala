package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse
import xml.{Node, XML}

/**
 * @author janmachacek
 */

trait XhtmlPayload extends PayloadRegistryAccess {
  register(parseXhtml _)

  def parseXhtml(response: MockHttpServletResponse) = {
    if (response.getContentType.startsWith("text/html"))
      Some(new XhtmlWebObjectBody(response.getContentAsString))
    else
      None
  }

}

class XhtmlWebObjectBody(payload: String) extends WebObjectBody[String, String](payload) {
  private val body = XML.loadString(payload)

  def <<[BB >: String, EE >: String](selector: String, value: String) = this

  def >>[R >: String](selector: String) = {
    require(selector != null)
    require("" != selector)

    def findElementBy(attribute: String, value: String) = {
      val input = body \\ "_" find { n =>
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
      case '/' => Some((body \\ selector).text)
      case '#' =>
        findElementValue(findElementBy("id", selector.substring(1)))
      case '.' =>
        findElementValue(findElementBy("class", selector.substring(1)))
      case _ =>
        findElementValue(findElementBy("class", selector))
    }

  }

  def >>![R >: String](selector: String) = >>(selector).get
}