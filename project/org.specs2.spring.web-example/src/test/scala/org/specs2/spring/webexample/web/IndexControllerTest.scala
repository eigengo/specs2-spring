package org.specs2.spring.webexample.web

import org.specs2.spring.web.{WebContextConfiguration, Specification}
import org.specs2.spring.webexample.services.ManagementService
import org.springframework.beans.factory.annotation.Autowired
import org.specs2.spring.webexample.domain.User
import org.springframework.test.context.ContextConfiguration

/**
 * @author janmachacek
 */
@WebContextConfiguration(Array("classpath*:/META-INF/spring/module-context.xml"))
//@ContextConfiguration()
class IndexControllerTest extends Specification {
  @Autowired
  var managementService: ManagementService = _

  "web roundtrip test" in {
    this.managementService.findAll(classOf[User]) must beEmpty

    post("/users.html", Map("username" -> "aaaa", "fullName" -> "Jan"))
    val wo = get("/users/1.html")
    post(wo << ("#fullName", "Jan Machacek"))

    success
  }

}