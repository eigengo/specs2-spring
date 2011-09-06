package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.hibernate3.{HibernateCallback, HibernateTemplate}
import org.hibernate.Session
import org.specs2.execute.Result

/**
 * @author janmachacek
 */

trait SqlDataAccess {
  private[spring] def getJdbcTemplate: JdbcTemplate

  def setup = {

  }

}

trait HibernateDataAccess {
  private[spring] def getHibernateTemplate: HibernateTemplate

  def deleteAll(entity: Class[_]) {
//    getHibernateTemplate.execute(new HibernateCallback[Void] {
//      def doInHibernate(session: Session) = {
//
//        null
//      }
//    })
  }

  import org.specs2.execute._

  def insert[T]: (T => Result) = {
    {t => getHibernateTemplate.saveOrUpdate(t); Success("ok")}
  }

  def insert[T](f: T => Any): (T => Result) = {
    {t =>
      f(t)
      getHibernateTemplate.saveOrUpdate(t)
      Success("ok")
    }
  }

}