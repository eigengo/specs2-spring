package org.specs2.spring.web

import org.specs2.spring.{TestTransactionDefinitionExtractor, EnvironmentExtractor, JndiEnvironmentSetter}
import org.specs2.spring.TestTransactionDefinitionExtractor.TestTransactionDefinition
import org.springframework.transaction.PlatformTransactionManager
import org.specs2.specification.Example
import org.springframework.mock.web.{MockHttpSession, MockHttpServletResponse}
import org.springframework.web.servlet.ModelAndView

/**
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification with PayloadRegistry {
  private val testContext = new TestContext()

  def x(x: AnyRef) {
    println("registered " + x)
  }

  def g(contentType: String, body: Array[Byte]): String = {
    for (f <- this.payloadFunctions) {
      val s = f(contentType, body)
      if (s.isDefined) return s.get
    }

    "unknown"
    // throw new RuntimeException("Did not understand " + contentType + ". Include the appropriate trait.")
  }

  override def is: org.specs2.specification.Fragments = {
    // setup the specification's environment
    new JndiEnvironmentSetter().prepareEnvironment(new EnvironmentExtractor().extract(this))
    this.testContext.setup(this)

    val ttd = new TestTransactionDefinitionExtractor().extract(this)
    if (ttd == TestTransactionDefinition.NOT_TRANSACTIONAL)
      // no transactions required
      this.specFragments
    else {
      // transactions required, run each example body in a [separate] transaction
      val transactionManager = this.testContext.getBean(classOf[PlatformTransactionManager])

      this.specFragments.map {
        f =>
          f match {
            case Example(desc, body) =>
              Example(desc, {
                val transactionStatus = transactionManager.getTransaction(ttd.getTransactionDefinition)
                try {
                  val result = body()
                  if (!ttd.isDefaultRollback) transactionManager.commit(transactionStatus)
                  result
                } finally {
                  if (ttd.isDefaultRollback) transactionManager.rollback(transactionStatus)
                }
              })
            case _ => f
          }
      }
    }
  }

  def post(url: String, params: Map[String, Any]) = {
    val request = new JspCapableMockHttpServletRequest("POST", url,
      this.testContext.getDispatcherServlet.getServletConfig)
    params.foreach {e => request.setParameter(e._1, e._2.toString)}

    doService(request)
  }

  def get(url: String, params: Any*) = {
    val request = new JspCapableMockHttpServletRequest("GET", String.format(url, params),
      this.testContext.getDispatcherServlet.getServletConfig)
    // request.setSession(this.httpSession)
    doService(request)
  }

  private def doService(request: JspCapableMockHttpServletRequest) = {
    try {
      val response = new MockHttpServletResponse()
      val dispatcherServlet = this.testContext.getDispatcherServlet

      val requestThread = new Thread(new Runnable() {
        def run() {
          // SecurityContextHolder.getContext().setAuthentication(auth)
          dispatcherServlet.service(request, response)
        }

      }, "Web Thread");
      requestThread.start()
      requestThread.join()

      val modelAndView = request.getAttribute(TracingDispatcherServlet.MODEL_AND_VIEW_KEY).asInstanceOf[ModelAndView]
      val s = g(response.getContentType, response.getContentAsByteArray)
      println(s)

      new WebObject(request, response, modelAndView)
    } catch {
      case e: Exception => throw new RuntimeException(e);
    }
  }

}