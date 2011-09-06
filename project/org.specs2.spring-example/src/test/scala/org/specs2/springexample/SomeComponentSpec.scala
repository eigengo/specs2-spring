package org.specs2.springexample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.specs2.spring.{BeanTables, HibernateDataAccess, Specification}
import java.util.Date

/**
 * @author janmachacek
 */
@IntegrationTest
class SomeComponentSpec extends Specification with HibernateDataAccess with BeanTables {
  @Autowired var someComponent: SomeComponent = _
  @Autowired var hibernateTemplate: HibernateTemplate = _

  /*
  "Some such" in {
    "generate 10 users " ! generate(10)
  }
  */

  /*
  "Another test" in {
    "name" | "teamName" | "age" |
    "Jan" !! "Wheelers" ! 32    |
    "Ani" !! "Team GB"  ! 30    |> { rider: Rider =>
      "rider" | "time" |
      rider   ! new Date() |> insert[Entry]
    }

    // do stuff with the inserted objects
    this.hibernateTemplate.loadAll(classOf[Entry]) must have size(2)
  }
  */

  "Hibernate insert all" in {
    val riders =
     "age" | "name" | "teamName" |
      32   ! "Jan"  ! "Wheelers" |
      30   ! "Ani"  ! "Team GB"  |< { r: Rider =>
      "number" | "time"     |
       1       ! new Date() |
       2       ! new Date() |< { e: Entry => r.addEntry(e) }
    }

    this.hibernateTemplate.loadAll(classOf[Rider]) must have size(2)
  }

  "Hibernate insert all" in {
    val riders =
     "age" | "name" | "teamName" |
     32   ! "Jan"  ! "Wheelers" |
     30   ! "Ani"  ! "Team GB"  |<(classOf[Rider])

    print(riders)

    this.hibernateTemplate.loadAll(classOf[Rider]) must have size(2)
  }

  def generate(count: Int) = {
    this.someComponent.generate(count)
    this.hibernateTemplate.loadAll(classOf[Rider]) must have size (count)
  }

}