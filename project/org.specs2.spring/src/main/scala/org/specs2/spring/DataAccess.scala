package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.hibernate3.{HibernateCallback, HibernateTemplate}
import org.hibernate.Session

/**
 * @author janmachacek
 */

trait SqlDataAccess {
  private[spring] def getJdbcTemplate: JdbcTemplate

  def setup = {

  }

}

trait HibernateDataAccess {
  //private[spring] def getHibernateTemplate: HibernateTemplate

  def deleteAll(entity: Class[_]) {
//    getHibernateTemplate.execute(new HibernateCallback[Void] {
//      def doInHibernate(session: Session) = {
//
//        null
//      }
//    })
  }

  def insert[T]: (T => Unit) = {
    {t => println("inserting " + t)}
  }

}