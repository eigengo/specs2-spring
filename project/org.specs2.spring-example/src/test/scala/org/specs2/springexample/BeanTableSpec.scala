package org.specs2.springexample

import org.specs2.spring.BeanTables
import java.util.Date
import org.specs2.mutable.Specification
import scalaz._
import Scalaz._
import collection.immutable.HashMap

/**
 * @author janmachacek
 */
class BeanTableSpec extends Specification with BeanTables {

  /**
   * Trivial demonstration of BeanTables |> method
   */
  "Simple test" in {
    "age" | "name" | "teamName" |
      32 ! "Jan" ! "Wheelers" |
      30 ! "Ani" ! "Team GB" |> {
      r: Rider => r.getAge must be_>(29)
    }
  }

  /**
   * Demonstration of the |< (return-left) method that gives back a List of the appropriately constructed beans
   */
  "Simple list" in {
    val riders =
      "age" | "name" | "teamName" |
        32 ! "Jan" ! "Wheelers" |
        30 ! "Ani" ! "Team GB" |< (classOf[Rider])

    riders.size must be_==(2)
  }

  def clusterBy[A, B](l: List[A])(f: A => B) =
    (l∘(e => (f(e), e)))./:(new HashMap[B, List[A]])((r, c) => r + ((c._1, c._2 :: r.getOrElse(c._1, Nil)))).values

  "foo" in {
    val cluster = clusterBy(List("one", "two", "three", "eleven", "twelve"))(_.length)
    println(cluster)
    success
  }

  /**
   * Shows that the function supplied to the return-left method can be another BeanTable, with yet another
   * return-left function and that the entire construct returns the appropriately constructed beans
   */
  "Complex bean setup" in {
    val riders =
      "age" | "name" | "teamName" |
        32 ! "Jan" ! "Wheelers" |
        30 ! "Ani" ! "Team GB" |< {
        r: Rider =>
          "number" | "time" |
            1 ! new Date() |
            2 ! new Date() |< {
            e: Entry => r.addEntry(e)
          }
      }

    riders.size must be_==(2)
  }

}