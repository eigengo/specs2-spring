package org.specs2.spring.webexample.web

import org.specs2.spring.webexample.services.ManagementService
import org.springframework.beans.factory.annotation.Autowired
import org.specs2.spring.webexample.domain.User
import org.springframework.transaction.annotation.Transactional
import org.specs2.spring.web._
import org.specs2.web.{RR, XhtmlWebObjectBody, Xhtml}

/**
 * @author janmachacek
 */
@WebContextConfiguration(
  webContextLocations = Array("/WEB-INF/sw-servlet.xml"),
  contextLocations = Array("classpath*:/META-INF/spring/module-context.xml"))
@Transactional
class UserControllerSpecification extends Specification with DispatcherServletDispatcher {
  
  @Autowired
  var managementService: ManagementService = _

  "get roundtrip test" in {
    Xhtml(post)("/users.html", Map("id" -> "1", "username" -> "aaaa", "fullName" -> "Jan"))

    // this works
    val wox = G("/foo") as Xhtml()

    // I still want this to return ModelAndViewAwareWebObject[XhtmlWebObjectBody]
    val wo = Xhtml(get)("/foo")

    (wox.model[User].getFullName must_== ("Jan"))     ^
    (wox.model[User].getUsername must_== ("aaaa"))    ^
    (wox.body >>! ("#username") must_== ("aaaa"))
  }

  /*
  "roundtrip test" in {
    Xhtml(post)("/users.html", Map("id" -> "2", "username" -> "aaaa", "fullName" -> "Jan"))

    val wo = Xhtml(get)("/users/2.html")
    wo.body <<("#fullName", "Edited Jan")
    Xhtml(*)(wo.body)

    managementService.get(classOf[User], 2L).getFullName must_== ("Edited Jan")
  }
  */

}