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

  /*
  clusterBy :: Ord b => (a -> b) -> [a] -> [[a]]
  clusterBy f = M.elems . M.map reverse . M.fromListWith (++) . map (f &&& return)

   i.e. clusterBy(List("a", "bb", "cc"))(_.length) == List(List("a"), List("bb", "cc"))

   http://etorreborre.blogspot.com/2009/02/exercise-with-arrows-in-scala.html
   */
  def clusterBy[A, B](l: List[A])(f: A => B) = {
    val identity = (x: Any) => x
    groupByFirst(l ∘ { e => (f(e), e)})

    //    val identity = (x: A) => x
    //    identity.first apply(7, "abc")
  }

  def groupByFirst[A, B](l: List[(A, B)]) =
    l.foldLeft[Map[A, List[B]]](new HashMap[A, List[B]])(
      (res, cur) => res + ((cur._1, cur._2 :: res.getOrElse(cur._1, Nil)))).toList

  "foo" in {
    val cluster = clusterBy(List("one", "two", "three"))(_.length)
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