package org.specs2.spring.web

import org.springframework.web.servlet.ModelAndView
import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}
import org.specs2.web.{WebObjectBody, WebObject}

/**
 * @author janmachacek
 */
class ModelAndViewAwareWebObject[B <: WebObjectBody](
    val request: MockHttpServletRequest,
    val response: MockHttpServletResponse,
    val bodyOption: Option[B],
    val modelAndView: Option[ModelAndView]) extends WebObject[B](request, response, bodyOption) {

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
    def apply[T <: AnyRef](implicit evidence: ClassManifest[T]): T = {
      val i = modelMap.entrySet().iterator()
      val attributeType = evidence.erasure
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