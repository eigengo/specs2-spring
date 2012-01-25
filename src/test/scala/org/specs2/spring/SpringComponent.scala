package org.specs2.spring

import org.springframework.stereotype.Component
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

/**
 * @author janmachacek
 */

trait SpringComponent {
  def get[T](id: Long)(implicit evidence: ClassManifest[T]): T
  def save(entity: AnyRef): Unit
  def findAll[T](implicit evidence: ClassManifest[T]): java.util.List[T]
}

@Component
class SpringComponentImpl @Autowired() (private val sessionFactory: SessionFactory) extends SpringComponent {

  @Transactional(readOnly = true)
  def get[T](id: Long)(implicit evidence: ClassManifest[T]): T =
    sessionFactory.getCurrentSession.get(evidence.erasure, id).asInstanceOf[T]
  
  @Transactional
  def save(entity: AnyRef) {
    sessionFactory.getCurrentSession.saveOrUpdate(entity)
  }
  
  @Transactional(readOnly = true)
  def findAll[T](implicit evidence: ClassManifest[T]) = 
    sessionFactory.getCurrentSession.createCriteria(evidence.erasure).list().asInstanceOf[java.util.List[T]]

}