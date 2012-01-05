package org.specs2.spring

import org.springframework.test.context.ContextConfiguration
import org.specs2.mock.Mockito
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author janmachacek
 */
@ContextConfiguration(Array("classpath*:/META-INF/spring/module-context.xml"))
class SpecificationSpec extends Specification with Jndi with Mockito {
  @Autowired var springComponent: SpringComponent = _

  "springComponent works out meaning of life" in {
    springComponent.work must_==("42")
  }

}