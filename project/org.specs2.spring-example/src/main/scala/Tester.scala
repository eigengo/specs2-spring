import io.Source
import scalaz._
import Scalaz._
import java.io.File
/**
 * @author janmachacek
 */
object Tester {

  def main(args: Array[String]) {
    val files: String => List[File] = (dir) => new File(dir).listFiles().toList
    def lengths: Kleisli[List, File, Int] = ☆((f: File) => {
      if (f.isDirectory) lengths =<< f.listFiles().toList
      else Source.fromFile(f).getLines().toList ∘ (l => l.length())
    })

    val lineLengths = ☆(files) >=> lengths
    val homeLineLengths = ☆(files) >=> lengths <=< ((home: String) => List("/Users/" + home))
    val myLineLengths = (☆(files) >=> lengths) =<< (List("/Users/janmachacek/Tmp/foo"))

    println(lineLengths("/Users/janmachacek/Tmp/foo"))
    println(homeLineLengths("janmachacek/Tmp/foo"))
    println(myLineLengths)
  }

}
