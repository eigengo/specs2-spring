package org.specs2.springexample

import org.specs2.Specification
import org.specs2.spring.Spring
import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.transaction.TransactionConfiguration

/**
 * @author janmachacek
 */
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(Array("classpath*:/META-INF/spring/module-context.xml"))
class SomeComponentSpec extends Specification with Spring {
	@Autowired var someComponent: SomeComponent = _
	@Autowired var hibernateTemplate: HibernateTemplate = _

	def is =

	"SomeComponent should"                    ^
	"	generate ${count} users "             ! generate(100)
	end

	def generate(count: Int) = {
		this.someComponent.generate(count)
		this.hibernateTemplate.loadAll(classOf[Rider]) must have size(count)
	}

}