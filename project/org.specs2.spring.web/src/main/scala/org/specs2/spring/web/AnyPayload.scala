package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse

/**
 * @author janmachacek
 */

trait AnyPayload extends PayloadRegistryAccess {
  register(parseAnything _)

  private val nullWebObjectBody = new WebObjectBody[Null, Null](null) {

    def >>[R](selector: String) = throw new RuntimeException("Unknown body")

    def >>![R](selector: String) = throw new RuntimeException("Unknown body")

    def <<[BB >: Null, EE >: Null](selector: String, value: String) = throw new RuntimeException("Unknown body")
  }

  def parseAnything(response: MockHttpServletResponse) = Some(this.nullWebObjectBody)

}
