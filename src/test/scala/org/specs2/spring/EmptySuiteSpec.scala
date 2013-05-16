package org.specs2.spring

class EmptySuiteSpec extends Specification {

  "This test doesn't want to run" should {
    "Show you other thing that '0 example, 0 failure, 0 error' in SBT console or 'Empty test suite' in IntelliJ" in {
      success
    }
  }
}