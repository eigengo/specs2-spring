package org.specs2.spring

// N.B. Adding @Transactional should cause the test to fail, because
// it does not load ApplicationContext using the @ContextConfiguration
// annotation
//@Transactional
class NoSuchMethodArgsSpec extends SpecificationLike {

  "this spec" should {
    "do nothing" in {
      success
    }
  }

}
