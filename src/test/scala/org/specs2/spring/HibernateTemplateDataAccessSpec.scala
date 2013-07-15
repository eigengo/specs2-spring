package org.specs2.spring

import domain.User
import org.specs2.mock.Mockito
import org.springframework.orm.hibernate3.HibernateTemplate

/**
 * @author anirvanchakraborty
 */
class HibernateTemplateDataAccessSpec extends SpecificationLike with HibernateTemplateDataAccess with BeanTables with Mockito {
  implicit val template = mock[HibernateTemplate]

  "hibernate3 based " in {
    "insert works as expected" in {
      "username" | "firstName" |
        "doo"     !! "bar"       |
        "doo"     !! "bar"       |> insert[User]

      there were two (template).saveOrUpdate(new User {username="doo"; firstName="bar"})
    }

    "deleteAll works as expected" in {
      val users =
      "username" | "firstName" |
        "doo"     !! "bar"       |
        "doo"     !! "bar"       |< classOf[User]
      deleteAll[User]
//      there were two (template).execute <-- Need to find what's best to test here
      success
    }
  }
  
}