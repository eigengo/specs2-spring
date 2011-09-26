package org.specs2.spring.web

import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}
import org.springframework.web.servlet.ModelAndView

/**
 * Represents processed and pre-chewed HttpServletResponse so that you can write meaningful
 * code in your examples.
 * The instances of {{WebObject}} carry the originating {{request}}, the matching {{response}},
 * together with extracted {{modelAndView}} and a pre-processed {{body}}.
 * The methods {{&lt;&lt;}}, {{&gt;&gt;}} and {{&gt;&gt;!}} manipulate the {{body}}, allowing you to
 * get or set the value of some HTML element, execute arbitrary JavaScript, examine the PDF, ... (depending
 * on what payload processing traits you mixin to your test).
 * Finally, the {{model}} and {{modelOption}} give you access to the model elements of the {{ModelAndView}}
 * returned from the controller processing.
 *
 * @author janmachacek
 */
class WebObject[B <: WebObjectBody](val request: MockHttpServletRequest,
                 val response: MockHttpServletResponse,
                 val modelAndView: Option[ModelAndView],
                 val body: Option[B]) {

  /**
   * Gets the convenient wrapper around the Spring model portion of the {{ModelAndView}}
   *
   * @return the model of the {{ModelAndView}}, if available
   */
  def modelOption = if (modelAndView == None) None else Some(model)

  /**
   * Gets the convenient wrapper around the Spring model portion of the {{ModelAndView}}
   *
   * @return the model
   */
  def model = new Model(modelAndView.get.getModel)

  /**
   * Returns the payload of the web object's body
   *
   * @return the body of the response
   */
  def ! = body.get

  /**
   * Convenient wrapper around the model portion of the {{ModelAndView}}
   */
  class Model(modelMap: java.util.Map[String, AnyRef]) {

    /**
     * Selects the value by its name in the model
     *
     * @param attributeName the name of the attribute to find in the model
     * @return the value in the model
     */
    def apply[T](attributeName: String) = {
      modelMap.get(attributeName).asInstanceOf[T]
    }

    /**
     * Selects the value by its type in the model; if there is exactly one value
     * of the given type
     *
     * @param attributeType the type of the attribute in the model
     * @return the value in the model
     */
    def apply[T <: AnyRef](attributeType: Class[T]): T = {
      val i = modelMap.entrySet().iterator()
      while (i.hasNext) {
        val e = i.next
        if (e.getValue != null && e.getValue.getClass == attributeType) {
          return e.getValue.asInstanceOf[T]
        }
      }
      
      throw new RuntimeException("No element type " + attributeType + " found in the model.")
    }
  }

}

/**
 * Models the body of the WebObject--it is the chewed-over response bytes
 */
abstract class WebObjectBody {

}
