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
class IndexControllerTest extends Specification with XhtmlPayload with JavaScriptPayload {
  
  @Autowired
  var managementService: ManagementService = _

  "web roundtrip test" in {
    this.managementService.findAll(classOf[User]).isEmpty must beTrue

    post("/users.html", Map("username" -> "aaaa", "fullName" -> "Jan"))

    val wo = get("/users/1.html", Map())
    wo.model(classOf[User]).getFullName must_== ("Jan")
    wo.model(classOf[User]).getUsername must_== ("aaaa")

    (wo >>! "#username") must_== ("aaaa")
  }

}