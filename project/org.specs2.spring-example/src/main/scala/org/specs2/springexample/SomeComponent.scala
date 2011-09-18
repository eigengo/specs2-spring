package org.specs2.springexample

import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author janmachacek
 */
@Component
class SomeComponent @Autowired()(private val hibernateTemplate: HibernateTemplate) {

  def generate(count: Int) {
    for (c <- 0 until count) {
      val rider = new Rider()
      rider.setName("Rider #" + c)
      this.hibernateTemplate.saveOrUpdate(rider)
    }
  }

}