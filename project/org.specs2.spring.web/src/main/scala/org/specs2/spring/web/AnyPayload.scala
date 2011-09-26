package org.specs2.spring.web

import org.springframework.mock.web.MockHttpServletResponse

/**
 * A no-operation payload that will deal with any responses, but without offering any
 * useful capabilities.
 *
 * @author janmachacek
 */
trait AnyPayload extends PayloadRegistryAccess {
  register(parseAnything _)

  private val nullWebObjectBody = new WebObjectBody[Null, Null](null) {

    def >>[R](selector: String) = throw new RuntimeException("Unknown body")

    def <<[BB >: Null, EE >: Null](selector: String, value: Any) = throw new RuntimeException("Unknown body")
  }

  /**
   * Returns a no-op parser for every response.
   *
   * @param response the HTTP response to get the body for
   * @return the no-op {{WebObjectBody}} implementation
   */
  def parseAnything(response: MockHttpServletResponse) = Some(this.nullWebObjectBody)

}
