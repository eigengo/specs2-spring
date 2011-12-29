package org.specs2.springexample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.specs2.spring.{BeanTables, HibernateDataAccess, Specification}

/**
 * Specification that creates the Spring ApplicationContext; the configuration for the context relies on some
 * entries in the JNDI tree, which the ``org.specs2.spring.Specification`` inserts according to the "instructions"
 * in the ``IntegrationTest`` annotation. Once the environment is set up, the test proceeds to autowire the
 * appropriately annotated fields ``someComponent`` and ``hibernateTemplate``).<br/>
 * Because this specification includes the ``Transactional`` annotation (indirectly: the ``IntegrationTest``
 * annotation includes the ``Transactional`` annotation), every example will execute in its transaction; the
 * transaction will be automatically rolled back at the end.
 *
 * @author janmachacek
 */
@IntegrationTest
class SomeComponentSpec extends Specification with HibernateDataAccess with BeanTables {
  @Autowired var someComponent: SomeComponent = _
  @Autowired var hibernateTemplate: HibernateTemplate = _

  /**
   * Demonstrates usage of specification with example
   */
  "Generate 10 riders" in {
    val count = 10

    this.someComponent.generate(count)
    this.someComponent.findAll(classOf[Rider]).size() must be_==(count)
  }

  /**
   * Shows the usage of BeanTables and HibernateDataAccess to set up and insert test objects
   * using the convenient tabular notation.
   */
  "Setup data & getByUsername" in {
    "age" | "username" | "name" | "teamName" |
      32 ! "janm" ! "Jan" ! "Wheelers" |
      30 ! "anic" ! "Ani" ! "Team GB" |> insert[Rider]

    this.someComponent.getByUsername("janm").getName must_== ("Jan")
    this.someComponent.getByUsername("anic").getName must_== ("Ani")
  }

}