package org.specs2.spring.web

import org.specs2.spring.{TestTransactionDefinitionExtractor, EnvironmentExtractor, JndiEnvironmentSetter}
import org.specs2.spring.TestTransactionDefinitionExtractor.TestTransactionDefinition
import org.springframework.transaction.PlatformTransactionManager
import org.specs2.specification.Example
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletResponse

/**
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification with PayloadRegistry {
  private val testContext = new TestContext()
  private val httpResponsesPayload = new HttpResponsesPayload

  def getWebObjectBody(response: MockHttpServletResponse): WebObjectBody[_, _] = {
    if (response.getStatus != HttpServletResponse.SC_OK)
      return this.httpResponsesPayload.parseHttpResponses(response).get

    for (f <- this.payloadFunctions) {
      val s = f(response)
      if (s.isDefined) return s.get
    }

    throw new RuntimeException("No body for " + response)
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
    params.foreach {
      e => request.setParameter(e._1, e._2.toString)
    }

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

      if (response.getRedirectedUrl != null) {
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
      }

      val modelAndView = request.getAttribute(TracingDispatcherServlet.MODEL_AND_VIEW_KEY).asInstanceOf[ModelAndView]
      val webObjectBody = getWebObjectBody(response)

      new WebObject(request, response, modelAndView, webObjectBody)
    } catch {
      case e: Exception => e.printStackTrace(); throw new RuntimeException(e);
    }
  }

}