package org.specs2.spring.webexample.web

import org.specs2.spring.web.{WebContextConfiguration, Specification}

/**
 * @author janmachacek
 */
@WebContextConfiguration(
  value = Array("classpath*:/META-INF/spring/module-context.xml")
)
class IndexControllerTest extends Specification {

  "some such" in {
    post("/users.html", Map("username" -> "aaaa", "fullName" -> "Jan"))
    val wo = get("/users/1.html")
    post(wo << ("#fullName", "Jan Machacek"))

    success
  }

}