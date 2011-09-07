package org.specs2.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.hibernate3.{HibernateCallback, HibernateTemplate}
import org.hibernate.Session

/**
 * @author janmachacek
 */

trait SqlDataAccess {
  private[spring] def getJdbcTemplate: JdbcTemplate


}

/**
 * Convenience mixin for using Hibernate in your Spring integration tests; includes the {@link #insert} method overloads
 * that work well with {@link BeanTables}.
 */
trait HibernateDataAccess {
  private[spring] def getHibernateTemplate: HibernateTemplate

  /**
   * Removes all entities of the given type
   *
   * @param entity implicitly supplied class manifest of the entity type to be deleted
   */
  def deleteAll[T](implicit entity: ClassManifest[T]) {
    getHibernateTemplate.execute(new HibernateCallback[Void] {
      def doInHibernate(session: Session) = {
        session.createQuery("delete from " + entity.erasure.getName).executeUpdate()
        null
      }
    })
  }

  import org.specs2.execute._

  /**
   * Returns a function that inserts the object and returns Success; the function can be supplied to the BeanTables
   * {@code |>} method.<br/>
   * Typical usage is
   * <pre>
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
  def insert[T]: (T => Result) = {
    {t => getHibernateTemplate.saveOrUpdate(t); Success("ok")}
  }

  /**
   * Returns a function that runs the supplied function f on the object; then inserts the object and returns Success;
   * the function can be supplied to the BeanTables {@code |>} method.<br/>
   * Typical usage is
   * <pre>
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
   * @param f function that operates on the instance {@code T}; this function will run before the Hibernate save.
   * @return function that inserts the object and returns Success when the insert succeeds.
   */
  def insert[T](f: T => Any): (T => Result) = {
    {t =>
      f(t)
      getHibernateTemplate.saveOrUpdate(t)
      Success("ok")
    }
  }

}