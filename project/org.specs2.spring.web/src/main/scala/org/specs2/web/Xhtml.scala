package org.specs2.web

import Specification._
import org.springframework.mock.web.{MockHttpServletRequest, MockHttpServletResponse}
import xml.{Node, XML}

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
    Some(new XhtmlWebObjectBody(response.getContentAsString, scala.collection.mutable.Map()))
}

/**
 * Response body that assumes that the body is the XHTML of the returned page.
 * The supported selectors in the {{<<}}, {{>>}} and {{>>!}} operators are
 * <ul>
 *   <li>#{id} selects element whose <i>id</i> matches the given {id}</li>
 *   <li>.{id} selects element whose <i>class</i> matches the given {id}</li>
 *   <li>:{id} selects element whose <i>name</i> matches the given {id}</li>
 *   <li>/{xpath} selects element in the XPath</li>
 * </ul>
 *
 * @param payload the XHTML string
 */
class XhtmlWebObjectBody(val body: String, val params: scala.collection.mutable.Map[String, List[String]]) extends WebObjectBody with Requestable  {
  private val dom = XML.loadString(body)
  private val ElementSelector = "(\\w+)?\\@([^=]+)=?(.*)?".r

  private def getAttributeValue(node: Node, attribute: String) = {
    val as = node.attribute(attribute)
    if (as != None) Some(as.get.text) else None
  }

  private def requestParameterNode(node: Node) = {
    (getAttributeValue(node, "id"), getAttributeValue(node, "name"), getAttributeValue(node, "value"))
  }

  def request(request: MockHttpServletRequest) = {
    val forms = dom \\ "form"
    if (forms.length == 0) throw new NoFormException
    if (forms.length > 1) throw new MultipleFormsException
    
    def addParameter(request: MockHttpServletRequest, param: String, value: String) {
      val realValue = if (params.get(param) != None) params.get(param).get.toArray else Array(value)
      request.addParameter(param, realValue)
    }
    
    dom \\ "input" foreach { n =>
      requestParameterNode(n) match {
        case (None, Some(name), Some(value)) =>
          addParameter(request, name, value)
        case (Some(id), _, Some(value)) =>
          addParameter(request, id, value)
        case _ =>
      }
    }
    
    request.setRequestURI(getAttributeValue(forms(0), "action").get)
    val method = getAttributeValue(forms(0), "method")
    request.setMethod(if (method != None) method.get.toUpperCase else "POST")

    request
  }

  /**
   * Sets the value of the input element identified by {{selector}}
   * to the value {{value}}
   *
   * @param selector the selector that identifies the element to set
   * @param value the new value for the element
   * @return XhtmlWebObjectBody instance with the element set
   */
  def <<(selector: String, value: String) = {
    val realSelector = selector.charAt(0) match {
      case '#' => selector.substring(1)
      case ':' => selector.substring(1)
      case _ => selector
    }

    val values = params.get(realSelector)
    params.put(realSelector, if (values == None) List(value) else values.get :+ value)

    this
  }

  /**
   * Selects a form using the given {{selector}} and returns a new {{XhtmlWebObjectBody}} containing just the
   * selected form.
   *
   * @param selector the selector that identifies the form
   * @return XhtmlWebObjectBody instance with just the selected form
   */
  def form(selector: String) = {
    val form = select(selector)
    if (form == None)
      this
    else {
      new XhtmlWebObjectBody(form.get.mkString, params)
    }
  }

  /**
   * Computes the value of the element identified by {{selector}}
   *
   * @param selector the selector that identifies the element to get
   * @return the value of the element, fails if the element does not exist
   * @see {{>>(selector: String)}}
   */
  def >>!(selector: String) = >>(selector).get

  /**
   * Computes the value of the element identified by {{selector}}. The syntax of the {{selector}} is
   * <ul>
   *   <li>#<i>id</i> selects element whose <i>id</i> matches the given {id}</li>
   *   <li>.<i>id</i> selects element whose <i>class</i> matches the given {id}</li>
   *   <li>:<i>id</i> selects element whose <i>name</i> matches the given {id}</li>
   *   <li>[<i>element</i>]@<i>attribute</i>[=<i>value</i>] selects the {{element}} (if not empty, else all elements)
   *    that includes the specified {{attribute}} whose value is {{value}} (if not empty, else any value)
   *   </li>
   *   <li>/<i>xpath</i> selects element in the XPath</li>
   * </ul>
   *
   * @param selector the selector that identifies the element to get.
   * @return <i>optionally</i>, the value of the element
   */
  def >>(selector: String) = {
    def findElementValue(element: Option[Node]) = {
      if (element == None) None
      val attributes = element.get.attribute("value")
      if (attributes == None) None
      Some(attributes.get.head.text)
    }

    findElementValue(select(selector))
  }

  private def select(selector: String) = {
    require(selector != null)
    require("" != selector)

    def findElementBy(element: String, attribute: String, value: String) = {
      val elements = if (element != null) dom \\ element else dom \\ "_"

      val input = elements find {
        n =>
          val attrs = n.attribute(attribute)
          if (attrs == None) false
          else attrs.get exists {
            _.text == value
          }
      }

      input
    }

    selector.charAt(0) match {
      case '#' =>
        findElementBy(null, "id", selector.substring(1))
      case '.' =>
        findElementBy(null, "class", selector.substring(1))
      case ':' =>
        findElementBy(null, "name", selector.substring(1))
      case _ =>
        selector match {
          case ElementSelector(element, attribute, value) =>
            findElementBy(element, attribute, value)
          case _ =>
            findElementBy(null, "class", selector)
        }
    }

  }

}