package org.specs2.spring.web

import org.specs2.spring.{TestTransactionDefinitionExtractor, EnvironmentExtractor, JndiEnvironmentSetter}
import org.specs2.spring.TestTransactionDefinitionExtractor.TestTransactionDefinition
import org.springframework.transaction.PlatformTransactionManager
import org.specs2.specification.Example
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletResponse

/**
 * The Spring-based web application testing trait. It works just like the plain
 * {{org.specs2.spring.Specification}}, but allows you to test the user interface
 * (read the servlet side) of your Spring application as well.
 * Typically, you will include one of the <i>payloads</i> traits, which are capable
 * of analysing the responses, allowing you to further analyse the returned values.
 * <pre>
 * @WebContextConfiguration(
 *   webContextLocations = Array("/WEB-INF/sw-servlet.xml"),
 *   contextLocations = Array("classpath*:/META-INF/spring/module-context.xml"))
 * @Transactional
 * class IndexControllerTest extends Specification with XhtmlPayload with JavaScriptPayload {
 *   // the post, get, put, delete methods now understand XHTML and JavaScript payloads
 * }
 * </pre>
 *
 * @author janmachacek
 */
trait Specification extends org.specs2.mutable.Specification with PayloadRegistry {
  private val testContext = new TestContext()
  private val httpResponsesPayload = new HttpResponsesPayload

  private def getWebObjectBody(response: MockHttpServletResponse): WebObjectBody[_, _] = {
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

  /**
   * Perform the HTTP POST method on the URL with parameters passed as the request
   * parameters of the post.
   *
   * @param url the URL to post to; for example {{/users.html}}
   * @param params the post parameters; for example {{Map("username" -> "janm")}}
   * @return the {{WebObject}} representing the response
   */
  def post(url: String, params: Map[String, Any]) = {
    val request = new JspCapableMockHttpServletRequest("POST", url,
      this.testContext.getDispatcherServlet.getServletConfig)
    params.foreach {
      e => request.setParameter(e._1, e._2.toString)
    }

    doService(request)
  }

  /**
   * Perform the HTTP POST method on the URL.
   *
   * @param url the URL to get; for example {{/users/update.html}}
   * @return the {{WebObject}} representing the response
   */
  def post(url: String) = get(url, Map())

  /**
   * Perform the HTTP GET method on the URL with the request parameters passed
   * as the request parameters of the GET.
   *
   * @param url the URL to get; for example {{/users/view.html}}
   * @param params the GET parameters; for example {{Map("id" -> 5)}}
   * @return the {{WebObject}} representing the response
   */
  def get(url: String, params: Map[String, Any]) = {
    val request = new JspCapableMockHttpServletRequest("GET", url,
      this.testContext.getDispatcherServlet.getServletConfig)
    params.foreach {
      e => request.setParameter(e._1, e._2.toString)
    }
    // request.setSession(this.httpSession)
    doService(request)
  }

  /*
   * Perform the HTTP GET method on the URL.
   *
   * @param url the URL to get; for example {{/users/1.html}}
   * @return the {{WebObject}} representing the response
   *
  def get(url: String) = get(url, Map())
  */

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

      val unsafeMav = request.getAttribute(TracingDispatcherServlet.MODEL_AND_VIEW_KEY).asInstanceOf[ModelAndView]
      val mav = if (unsafeMav != null) Some(unsafeMav) else None
      val webObjectBody = getWebObjectBody(response)

      new WebObject(request, response, mav, webObjectBody)
    } catch {
      case e: Exception => e.printStackTrace(); throw new RuntimeException(e);
    }
  }

}