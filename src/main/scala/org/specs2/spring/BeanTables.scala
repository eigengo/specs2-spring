package org.specs2.spring

import org.specs2.execute._
import org.specs2.text.Trim._
import org.springframework.beans.{BeanWrapperImpl, BeanWrapper}
import java.util.{HashSet, ArrayList}

/**
 * @author janmachacek
 */
trait BeanTables {

  implicit def toTableHeader(a: String) = new TableHeader(List(a))

  implicit def toDataRow(a: Any) = BeanRow(List(a))

  case class TableHeader(titles: List[String]) {
    def ||(title: String) = copy(titles = this.titles :+ title)

    def |(title: String) = copy(titles = this.titles :+ title)

    def |(row: BeanRow) = new AnyRefTable(titles, List(row))

    def |>[T1](row: BeanRow) = new AnyRefTable(titles, List(row), execute = true)
  }

  abstract class Table(val titles: List[String], val execute: Boolean = false) {
    /**
     * @return the header as a | separated string
     */
    def showTitles = titles.mkString("|", "|", "|")

    /**
     * Collect the results of each row
     * @param results list of (row description, row execution result)
     * @return an aggregated Result from a list of results
     */
    protected def collect[R <% Result](results: List[(String, R)]): DecoratedResult[BeanTable] = {
      val result = allSuccess(results)
      val header = result match {
        case Success(_) => showTitles
        case other => "  " + showTitles
      }
      DecoratedResult(BeanTable(titles, results), result.updateMessage {
        header + "\n" +
          results.map((cur: (String, R)) => resultLine(cur._1, cur._2)).mkString("\n")
      })
    }

    /**@return the logical and combination of all the results */
    private def allSuccess[R <% Result](results: List[(String, R)]): Result = {
      results.foldLeft(Success(""): Result)((res, cur) => res and cur._2)
    }

    /**@return the status of the row + the values + the failure message if any */
    private def resultLine(desc: String, result: Result): String = {
      result.status + " " + desc + {
        result match {
          case Success(_) => ""
          case _ => " " + result.message
        }
      }
    }

    case class PropertyDescriptor(name: String, value: Any)

  }

  case class AnyRefTable(override val titles: List[String], rows: List[BeanRow], override val execute: Boolean = false) extends Table(titles, execute) {
    outer =>
    def |(row: BeanRow) = AnyRefTable(titles, outer.rows :+ row, execute)

    def |[B](f: (B) => Result)(implicit m: ClassManifest[B]) = executeRow(m, f, execute)

    def |>[B](f: (B) => Result)(implicit m: ClassManifest[B]) = executeRow(m, f, true)

    def |<[B](implicit m: ClassManifest[B]): List[B] = {
      rows map {d: BeanRow => d.makeBean[B](titles, m)}
    }

    def |<[B](m: Class[B]): List[B] =
      rows map {d: BeanRow => d.makeBean[B](titles, m)}

    def |<[B](f: (B) => Unit)(implicit m: ClassManifest[B]): List[B] = {
      rows map {d: BeanRow =>
        val b = d.makeBean[B](titles, m)
        f(b)
        b
      }
    }

    def executeRow[B, R <% Result](m: ClassManifest[B], f: (B) => R, exec: Boolean): DecoratedResult[BeanTable] = {
      if (exec)
        collect(rows map {
          (d: BeanRow) => (d.show, implicitly[R => Result].apply(f(d.makeBean(titles, m))))
        })
      else DecoratedResult(BeanTable(titles, Seq[BeanTableRow]()), Success("ok"))
    }
  }

  case class BeanRow(propertyValues: List[Any]) {
    def show = productIterator.mkString("|", "|", "|")

    def !(value: Any) = BeanRow(propertyValues :+ value)
    def !!(value: Any) = BeanRow(propertyValues :+ value)

    private [spring] def makeBean[T](propertyNames: List[String], m: Class[_]) = {
      val bean = m.newInstance().asInstanceOf[T]
      val wrapper = new BeanWrapperImpl(bean)

      for (i <- 0 until propertyNames.size) {
        val propertyName = propertyNames(i)
        val propertyValue = propertyValues(i)

        wrapper.setPropertyValue(propertyName, propertyValue)
      }

      bean
    }

    private [spring] def makeBean[T](propertyNames: List[String], m: ClassManifest[T]): T = makeBean(propertyNames, m.erasure)

  }

}

case class BeanTable(titles: Seq[String], rows: Seq[BeanTableRow]) {
  def isSuccess = rows.forall(_.isSuccess)
}
object BeanTable {
  def apply[R <% Result](titles: Seq[String], results: Seq[(String, R)]): BeanTable = BeanTable(titles, results.collect { case (v, r) => BeanTableRow(v, r) })
}
case class BeanTableRow(cells: Seq[String], result: Result) {
  def isSuccess = result.isSuccess
}
object BeanTableRow {
  def apply[R](values: String, result: R)(implicit convert: R => Result): BeanTableRow = BeanTableRow(values.trimEnclosing("|").splitTrim("\\|"), convert(result))
}
