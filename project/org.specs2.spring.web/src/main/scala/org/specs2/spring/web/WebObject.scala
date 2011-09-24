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

  class Model(modelMap: java.util.Map[String, AnyRef]) {

    def apply[T](attributeName: String) = {
      modelMap.get(attributeName).asInstanceOf[T]
    }

    def apply[T <: AnyRef](attributeType: Class[T]) = {
      val i = modelMap.entrySet().iterator()
      var result: AnyRef = null
      while (i.hasNext) {
        val e = i.next
        if (e.getValue != null && e.getValue.getClass == attributeType) {
          result = e.getValue
        }
      }
      
      result.asInstanceOf[T]
    }
  }

}