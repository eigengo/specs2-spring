package org.specs2.spring.web

import xml.{Node, XML}
import Specification._
import org.springframework.mock.web.MockHttpServletResponse

/**
 * XHTML response companion object
 */
object Xhtml {

  /**
   * Processes the input to return the {{Xhtml}} instance that will deal with
   * the request. The {{Xhtml}} class can process and manipulate the XHTML response
   *
   * @param r the RR object representing the request
   * @return the {{Raw}} instance that will deal with the request
   */
  def apply(r: RR) = new Xhtml(r)

}

/**
 * XHTML response object that returns {{XHtmlWebObjectBody}} from the body
 * of the response.
 *
 * @param r the {{RR}} instance
 */
class Xhtml(r: RR) extends AbstractRR[XhtmlWebObjectBody](r) {

  def makeBody(response: MockHttpServletResponse) = 
    Some(new XhtmlWebObjectBody(response.getContentAsString))
}

/**
 * Response body that assumes that the body is the XHTML of the returned page.
 * The supported selectors in the {{<<}}, {{>>}} and {{>>!}} operators are
 * <ul>
 *   <li>#{id} selects element whose id matches the given {id}</li>
 *   <li>.{id} selects element whose class matches the given {id}</li>
 *   <li>[{id}] selects element whose name matches the given {id}</li>
 *   <li>/{xpath} selects element in the XPath</li>
 * </ul>
 *
 * @param payload the XHTML string
 */
class XhtmlWebObjectBody(val body: String) extends WebObjectBody {
  private val dom = XML.loadString(body)

  /**
   * Sets the value of the input element identified by {{selector}}
   * to the value {{value}}
   *
   * @param selector the selector that identifies the element to set
   * @param value the new value for the element
   * @return XhtmlWebObjectBody instance with the element set
   */
  def <<(selector: String, value: Any) = this

  /**
   * Computes the value of the element identified by {{selector}}
   *
   * @param selector the selector that idenfieids the element to get
   * @return the value of the element, fails if the element does not exist
   */
  def >>!(selector: String) = >>(selector).get

  /**
   * Computes the value of the element identified by {{selector}}
   *
   * @param selector the selector that idenfieids the element to get
   * @return optionally, the value of the element
   */
  def >>(selector: String) = {
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