package org.specs2.spring.web

import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}
import org.springframework.web.servlet.ModelAndView

/**
 * @author janmachacek
 */
class WebObject[B, E](val request: MockHttpServletRequest,
                 val response: MockHttpServletResponse,
                 val modelAndView: ModelAndView,
                 val body: WebObjectBody[B, E]) {

  def model = new Model(modelAndView.getModel)

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
