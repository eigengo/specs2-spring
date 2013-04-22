package org.specs2.spring

import org.springframework.stereotype.Component
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import scala.reflect.ClassTag

/**
 * @author janmachacek
 */

trait SpringComponent {
  def get[T](id: Long)(implicit evidence: ClassTag[T]): T
  def save(entity: AnyRef)
  def findAll[T](implicit evidence: ClassTag[T]): java.util.List[T]
}

@Component
class SpringComponentImpl @Autowired() (private val sessionFactory: SessionFactory) extends SpringComponent {

  @Transactional(readOnly = true)
  def get[T](id: Long)(implicit evidence: ClassTag[T]): T =
    sessionFactory.getCurrentSession.get(evidence.runtimeClass, id).asInstanceOf[T]
  
  @Transactional
  def save(entity: AnyRef) {
    sessionFactory.getCurrentSession.saveOrUpdate(entity)
  }
  
  @Transactional(readOnly = true)
  def findAll[T](implicit evidence: ClassTag[T]) =
    sessionFactory.getCurrentSession.createCriteria(evidence.runtimeClass).list().asInstanceOf[java.util.List[T]]

}