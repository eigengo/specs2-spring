package org.specs2.springexample

import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.hibernate.criterion.{Restrictions, DetachedCriteria}

/**
 * @author janmachacek
 */
@Component
class SomeComponent @Autowired()(private val hibernateTemplate: HibernateTemplate) {

  def findAll(entityType: Class[_]) =
    this.hibernateTemplate.loadAll(entityType)

  def generate(count: Int) {
    for (c <- 0 until count) {
      val rider = new Rider()
      rider.setName("Rider #" + c)
      rider.setUsername("user " + c)
      this.hibernateTemplate.saveOrUpdate(rider)
    }
  }

  def getByUsername(username: String) = {
    val riders = this.hibernateTemplate.findByCriteria(DetachedCriteria.forClass(classOf[Rider]).add(Restrictions.eq("username", username)))
    riders.get(0).asInstanceOf[Rider]
  }

}