package org.specs2.springexample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.specs2.matcher.DataTables
import org.specs2.spring.{BeanTables, HibernateDataAccess, Specification}

/**
 * @author janmachacek
 */
@IntegrationTest
class SomeComponentSpec extends Specification with HibernateDataAccess with BeanTables {
  @Autowired var someComponent: SomeComponent = _
  @Autowired var hibernateTemplate: HibernateTemplate = _

  "Some such" in {
    "generate 10 users " ! generate(10)
  }

  "Another test" in {
    /*
    Here's what I want to do with BeanTables
    
    "name" | "teamName" | "age" |
    "Jan"  ! "Wheelers" ! 32    | 
    "Ani"  ! "Team GB"  ! 30    |> { rider: Rider => ... }
    */
    //"abc" |> { rider: Rider => println(rider) }
    "abc" |> insert[Rider]

    success
  }

  def generate(count: Int) = {
    this.someComponent.generate(count)
    this.hibernateTemplate.loadAll(classOf[Rider]) must have size (count)
  }

}