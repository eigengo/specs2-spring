package org.specs2.springexample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.specs2.spring.{BeanTables, HibernateDataAccess, Specification}
/**
 * Specification that creates the Spring ApplicationContext; the configuration for the context relies on some
 * entries in the JNDI tree, which the {@code org.specs2.spring.Specification} inserts according to the "instructions"
 * in the {@link IntegrationTest} annotation. Once the environment is set up, the test proceeds to autowire the
 * appropriately annotated fields ({@link #someComponent} and {@link #hibernateTemplate}).<br/>
 * Because this specification includes the {@link Transactional} annotation (indirectly: the {@code IntegrationTest}
 * annotation includes the {@code Transactional} annotation), every example will execute in its transaction; the
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
  "Some such" in {
    "generate 10 users " ! generate(10)
  }

  /**
   * Shows the usage of BeanTables and HibernateDataAccess to set up and insert test objects
   * using the convenient tabular notation.
   */
  "Hibernate insert all" in {
    "age" | "username"  | "name" | "teamName" |
      32  ! "janm"      ! "Jan"  ! "Wheelers" |
      30  ! "anic"      ! "Ani"  ! "Team GB"  |> insert[Rider]

    this.someComponent.getByUsername("janm").getName must_== ("Jan")

  }

  /**
   * Example that calls the {@code generate} method and verifies that it generated the expected
   * number of users.
   */
  def generate(count: Int) = {
    this.someComponent.generate(count)
    this.hibernateTemplate.find("from Rider").size() must be_==(count)
  }

}