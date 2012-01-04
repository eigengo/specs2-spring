package org.specs2.spring.webexample.web

import org.specs2.spring.webexample.services.ManagementService
import org.springframework.beans.factory.annotation.Autowired
import org.specs2.spring.webexample.domain.User
import org.springframework.transaction.annotation.Transactional
import org.specs2.spring.web._
/**
 * @author janmachacek
 */
@WebContextConfiguration(
  webContextLocations = Array("/WEB-INF/sw-servlet.xml"),
  contextLocations = Array("classpath*:/META-INF/spring/module-context.xml"))
@Transactional
class UserControllerSpecification extends Specification {

  @Autowired
  var managementService: ManagementService = _

  "get roundtrip test" in {
    Xhtml(post)("/users.html", Map("id" -> "1", "username" -> "aaaa", "fullName" -> "Jan"))

    val wo = Xhtml(get)("/users/1.html")
    (wo.model[User].getFullName must_== ("Jan"))     ^
    (wo.model[User].getUsername must_== ("aaaa"))    ^
    (wo.body >>! ("#username") must_== ("aaaa"))
  }

  "roundtrip test" in {
    Xhtml(post)("/users.html", Map("id" -> "2", "username" -> "aaaa", "fullName" -> "Jan"))

    val wo = Xhtml(get)("/users/2.html")
    wo.body <<("#fullName", "Edited Jan")
    Xhtml(*)(wo.body)
    
    managementService.get(classOf[User], 2L).getFullName must_== ("Edited Jan")
  }

}