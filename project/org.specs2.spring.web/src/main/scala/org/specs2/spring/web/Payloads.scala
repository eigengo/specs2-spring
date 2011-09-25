package org.specs2.spring.web

import collection.mutable.MutableList

/**
 * @author janmachacek
 */
object Payloads {
  type PayloadFunction = (String, Array[Byte]) => Option[String]
  val payloadFunctions = new MutableList[PayloadFunction]

  def x(f: PayloadFunction) {
    this.payloadFunctions += f
  }

  def g(contentType: String, body: Array[Byte]): String = {
    for (f <- this.payloadFunctions) {
      val s = f(contentType, body)
      if (s.isDefined) return s.get
    }

    "unknown"
    // throw new RuntimeException("Did not understand " + contentType + ". Include the appropriate trait.")
  }

}