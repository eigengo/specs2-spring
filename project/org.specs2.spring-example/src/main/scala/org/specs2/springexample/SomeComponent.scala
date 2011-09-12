package org.specs2.springexample

import org.springframework.orm.hibernate3.HibernateTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scalaz._
import Scalaz._
import java.io.{FileReader, Reader, File, BufferedReader}

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

    //(listArr(f &&& identity) >>> arr(groupByFirst) >>> arr(second) >>> arr(reverse))(l)

//  def bufferFile(f: File) = IO {
//    new BufferedReader(new FileReader(f))
//  }
//
//  def closeReader(r: Reader) = IO {
//    r.close
//  }
//
//  def bracket[A, B, C](init: IO[A], fin: A => IO[B], body: A => IO[C]): IO[C] =
//    for {a <- init
//         c <- body(a)
//         _ <- fin(a)}
//    yield c
//
//  def enumFile[A](f: File, i: IterV[String, A]): IO[IterV[String, A]] =
//    bracket(bufferFile(f),
//      closeReader(_: BufferedReader),
//      enumReader(_: BufferedReader, i))
//

}