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
class WebObject[B, E](val request: MockHttpServletRequest,
                 val response: MockHttpServletResponse,
                 val modelAndView: Option[ModelAndView],
                 val body: WebObjectBody[B, E]) {

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

  def <<(selector: String, value: String) =
    new WebObject(request, response, modelAndView, body << (selector, value))

  def >>(selector: String) = body >> selector

  def >>!(selector: String) = body >>! selector

  def payload = body.payload

  class Model(modelMap: java.util.Map[String, AnyRef]) {

    def apply[T](attributeName: String) = {
      modelMap.get(attributeName).asInstanceOf[T]
    }

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

abstract class WebObjectBody[+B, +E](val payload: B) {

  def <<[BB >: B, EE >: E](selector: String, value: String): WebObjectBody[BB, EE]

  def >>[R >: E](selector: String): Option[R]

  def >>![R >: E](selector: String): R

}
