package org.specs2.spring

import org.springframework.orm.hibernate3.{HibernateCallback, HibernateTemplate}
import org.hibernate.{HibernateException, SessionFactory, Session}
import org.specs2.execute.{Success, Result}
import scala.reflect.ClassTag

/**
 * @author janmachacek
 */
trait SqlDataAccess {


}

/**
 * Convenience mixin for using Hibernate in your Spring integration tests; includes the ``insert`` method overloads
 * that work well with ``org.specs2.spring.BeanTables``.
 */
trait HibernateDataAccess {

  private def inSession[A](sessionFactory: SessionFactory)(f: (Session) => A) = {
    def openSession = {
      try {
        (sessionFactory.getCurrentSession, true)
      } catch {
        case e: HibernateException =>
          (sessionFactory.openSession(), false)
      }
    }
    
    val (session, joinedExisting) = openSession
    
    val ret = f(session)
    
    if (!joinedExisting) {
      session.flush()
      session.close()
    }
    
    ret
  }
  
  /**
   * Removes all entities of the given type
   *
   * @param entity implicitly supplied class manifest of the entity type to be deleted
   * @param sessionFactory the session factory that will have the entities removed
   */
  def deleteAll[T](implicit entity: ClassTag[T], sessionFactory: SessionFactory) {
    inSession(sessionFactory) { s =>
      s.createQuery("delete from " + entity.runtimeClass.getName).executeUpdate()
    }
  }

  import org.specs2.execute._

  /**
   * Returns a function that inserts the object and returns Success; the function can be supplied to the BeanTables
   * ``|>`` function.
   * Typical usage is
   * <pre>
   *  implicit var sessionFactory = make-SessionFactory-instance()
   *
   *  "Some service operation" in {
   *    "age" | "name" | "teamName" |
   *     32   ! "Jan"  ! "Wheelers" |
   *     30   ! "Ani"  ! "Team GB"  |> insert[Rider]
   *
   *    // tests that rely on the inserted Rider objects
   *    success
   *  }
   *  </pre>
   *
   * @return function that inserts the object and returns Success when the insert succeeds.
   */
  def insert[T](implicit sessionFactory: SessionFactory): (T => Result) = {
    t => inSession(sessionFactory) { s => s.saveOrUpdate(t); Success("ok") } 
  }

  /**
   * Returns a function that runs the supplied function f on the object; then inserts the object and returns Success;
   * the function can be supplied to the BeanTables ``|>`` function.<br/>
   * Typical usage is
   * <pre>
   *  implicit var sessionFactory = make-SessionFactory-instance()
   *
   *  "Some service operation" in {
   *    "age" | "name" | "teamName" |
   *     32   ! "Jan"  ! "Wheelers" |
   *     30   ! "Ani"  ! "Team GB"  |> insert[Rider] { r: Rider => r.addEntry(...) }
   *
   *    // tests that rely on the inserted Rider objects; each with one Entry inserted in the function given
   *    // to the insert[Rider] method
   *    success
   *  }
   *  </pre>
   *
   * @param f function that operates on the instance ``T``; this function will run before the Hibernate save.
   * @return function that inserts the object and returns Success when the insert succeeds.
   */
  def insert[T, R](f: T => R)(implicit sessionFactory: SessionFactory): (T => Result) = { 
    t =>
      f(t)
      inSession(sessionFactory) { _.saveOrUpdate(t) }
      Success("ok")
    }

}

trait HibernateTemplateDataAccess {
  
  /**
   * Returns a function that runs the supplied function f on the object; then inserts the object and returns Success;
   * the function can be supplied to the BeanTables ``|>`` function.<br/>
   * Typical usage is
   * <pre>
   *  implicit var hibernateTemplate = make-HibernateTemplate-instance()
   *
   *  "Some service operation" in {
   *    "age" | "name" | "teamName" |
   *     32   ! "Jan"  ! "Wheelers" |
   *     30   ! "Ani"  ! "Team GB"  |> insert[Rider] { r: Rider => r.addEntry(...) }
   *
   *    // tests that rely on the inserted Rider objects; each with one Entry inserted in the function given
   *    // to the insert[Rider] method
   *    success
   *  }
   *  </pre>
   *
   * @param f function that operates on the instance ``T``; this function will run before the Hibernate save.
   * @return function that inserts the object and returns Success when the insert succeeds.
   */
  def insert[T, R](f: T => R)(implicit hibernateTemplate: HibernateTemplate): (T => Result) = {
    t =>
      f(t)
      hibernateTemplate.saveOrUpdate(t) 
      Success("ok")
  } 

  /**
   * Returns a function that inserts the object and returns Success; the function can be supplied to the BeanTables
   * ``|>`` function.
   * Typical usage is
   * <pre>
   *  implicit var hibernateTemplate = make-HibernateTemplate-instance()
   *
   *  "Some service operation" in {
   *    "age" | "name" | "teamName" |
   *     32   ! "Jan"  ! "Wheelers" |
   *     30   ! "Ani"  ! "Team GB"  |> insert[Rider]
   *
   *    // tests that rely on the inserted Rider objects
   *    success
   *  }
   *  </pre>
   *
   * @return function that inserts the object and returns Success when the insert succeeds.
   */
  def insert[T](implicit hibernateTemplate: HibernateTemplate): (T => Result) = {
    t => hibernateTemplate.saveOrUpdate(t); Success("ok")
  } 

  /**
   * Removes all entities of the given type
   *
   * @param entity implicitly supplied class manifest of the entity type to be deleted
   * @param hibernateTemplate HibernateTemplate instance that will have the entities removed
   */
  def deleteAll[T](implicit entity: ClassTag[T], hibernateTemplate: HibernateTemplate) {
    hibernateTemplate.execute(new HibernateCallback[Int] {
      def doInHibernate(session: Session) = 
        session.createQuery("delete from " + entity.runtimeClass.getName).executeUpdate()
    })
  }

}