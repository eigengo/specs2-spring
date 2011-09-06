package org.specs2.springexample

import org.specs2.spring.{BeanTables, HibernateDataAccess, Specification}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import java.util.Date

/**
 * @author janmachacek
 */
@IntegrationTest
class BeanTableSpec extends Specification with HibernateDataAccess with BeanTables {

  "Simple test" in {
    "age" | "name" | "teamName" |
     32   ! "Jan"  ! "Wheelers" |
     30   ! "Ani"  ! "Team GB"  |> { r: Rider => r.getAge must be_>(29) }
  }

  "Simple list" in {
    val riders =
     "age" | "name" | "teamName" |
      32   ! "Jan"  ! "Wheelers" |
      30   ! "Ani"  ! "Team GB"  |<(classOf[Rider])

    riders.size must be_==(2)
  }

  "Complex bean setup" in {
    val riders =
     "age" | "name" | "teamName" |
      32   ! "Jan"  ! "Wheelers" |
      30   ! "Ani"  ! "Team GB"  |< { r: Rider =>
      "number" | "time"     |
       1       ! new Date() |
       2       ! new Date() |< { e: Entry => r.addEntry(e) }
    }

    riders.size must be_==(2)
  }

}