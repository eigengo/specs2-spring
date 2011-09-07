package org.specs2.springexample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.specs2.spring.{BeanTables, HibernateDataAccess, Specification}
import java.util.Date
import org.springframework.transaction.annotation.Transactional

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

  "Hibernate insert all" in {
    "age" | "name" | "teamName" |
      32 ! "Jan" ! "Wheelers" |
      30 ! "Ani" ! "Team GB" |> insert[Rider] { r: Rider =>
        "number" | "time" |
          1 ! new Date() |
          2 ! new Date() |< { e: Entry => r.addEntry(e) }
    }

    this.hibernateTemplate.find("from Rider").size() must be_==(2)
  }

  "Plain generate list" in {
    val riders =
      "age" | "name" | "teamName" |
        32 ! "Jan" ! "Wheelers" |
        30 ! "Ani" ! "Team GB" |< (classOf[Rider])

    riders(0).getAge must be_==(32)
    riders(1).getAge must be_==(30)
  }

  def generate(count: Int) = {
    this.someComponent.generate(count)
    this.hibernateTemplate.find("from Rider").size() must be_==(count)
  }

}