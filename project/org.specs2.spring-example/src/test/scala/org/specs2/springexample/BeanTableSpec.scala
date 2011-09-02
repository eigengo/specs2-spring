package org.specs2.springexample

import org.specs2.spring.{BeanTables, HibernateDataAccess, Specification}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate

/**
 * @author janmachacek
 */
@IntegrationTest
class BeanTableSpec extends Specification with HibernateDataAccess with BeanTables {

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

}