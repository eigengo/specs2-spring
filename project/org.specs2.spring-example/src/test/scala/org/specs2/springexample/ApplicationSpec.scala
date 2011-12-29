package org.specs2.springexample

import org.specs2.spring.{Specification, HibernateDataAccess, BeanTables}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.test.context.ContextConfiguration
import org.hsqldb.jdbc.JDBCDriver
import org.specs2.spring.annotation._


/**
 * @author janmachacek
 */
@Transactional
@TransactionConfiguration (defaultRollback = true)
@ContextConfiguration (Array ("classpath*:/META-INF/spring/module-context.xml") )
@UseProfile (Array ("ACU") )
@SystemEnvironment (Array ("efoo=bar;ebaz=null") )
@SystemProperties (Array ("pfoo=bar;pbaz=null") )
@DataSource(name = "java:comp/env/jdbc/test",
  driverClass = classOf[JDBCDriver], url = "jdbc:hsqldb:mem:test")
@TransactionManager(name = "java:comp/TransactionManager")
class ApplicationSpec extends Specification with HibernateDataAccess with BeanTables {
  @Autowired var regulations: LegalRegulations = _
  @Autowired var someComponent: SomeComponent = _
  
  "no-one dopes!" in {
    "age" | "username" | "name"    | "teamName" |
       32 ! "wheeler"  ! "Wheeler" ! "Wheelers" |
       30 ! "nemesis"  ! "Nemesis" ! "Baddies"  |> insert[Rider]

    val rider = this.someComponent.getByUsername("wheeler")
    regulations.hasDoped(rider) must be_== (false)
  }
  
}