package org.specs2.spring.web

import io.Source
import org.springframework.mock.web.MockHttpServletRequest

/**
 * @author janmachacek
 */
class XhtmlSpec extends Specification {

  private def loadResource(resource: String) = {
    Source.fromInputStream(classOf[XhtmlSpec].getResourceAsStream(resource)).mkString
  }

  "single form" in {
    val wob = new XhtmlWebObjectBody(loadResource("single-form.html"), scala.collection.mutable.Map())
    wob >>! ("#username") must_== ("A")
  }

  "form selector" in {
    val wob = new XhtmlWebObjectBody(loadResource("multiple-forms.html"), scala.collection.mutable.Map())
    val form = wob.form("@action=/url2.html")
    
    form >>! ("#password2") must_== ("B")
  }

  "make request" in {
    val wob = new XhtmlWebObjectBody(loadResource("single-form.html"), scala.collection.mutable.Map())
    wob << ("username", "Foo")
    val request = new MockHttpServletRequest()
    wob.request(request)

    request.getParameter("username") must_== ("Foo")
  }

}