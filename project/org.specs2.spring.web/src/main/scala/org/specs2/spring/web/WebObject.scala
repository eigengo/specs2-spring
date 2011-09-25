package org.specs2.spring.web

import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}
import org.springframework.web.servlet.ModelAndView

/**
 * @author janmachacek
 */
class WebObject(val request: MockHttpServletRequest,
                 val response: MockHttpServletResponse,
                 val modelAndView: ModelAndView) {

  def responseBytes = response.getContentAsByteArray
  def model = new Model(modelAndView.getModel)

  def << (selector: String, value: String) = {
    new WebObject(request, response, modelAndView)
  }

  def >> (selector: String) = {
    ""
  }

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