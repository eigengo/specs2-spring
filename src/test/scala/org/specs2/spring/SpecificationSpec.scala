package org.specs2.spring

import domain.User
import org.springframework.test.context.ContextConfiguration
import org.specs2.mock.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.SessionFactory

/**
 * @author janmachacek
 */
@ContextConfiguration(Array("classpath*:/META-INF/spring/module-context.xml"))
class SpecificationSpec extends Specification 
  with BeanTables with HibernateDataAccess with SettableEnvironment with Mockito {
  
  @Autowired implicit var sessionFactory: SessionFactory = _
  @Autowired var springComponent: SpringComponent = _

  "springComponent must:" in {
    val user = User()
    user.username = "foo"
    user.firstName = "Jan"
    user.lastName = "Machacek"
    
    "find all users" in {
      "username" | "firstName" |
      "janm"    !! "Jan"       |
      "marco"   !! "Marc"      |> insert[User]

      val users =
        "username" | "firstName" |
        "janm"    !! "Jan"       |
        "marco"   !! "Marc"      |<classOf[User]
      
      springComponent.findAll[User].size() must_== (2)
    }
    
    "be able to save a user" in {
      springComponent.save(user)
      springComponent.get[User](user.id) must_==(user)

      springComponent.findAll[User].size() must_== (3)
    }
  }
}
