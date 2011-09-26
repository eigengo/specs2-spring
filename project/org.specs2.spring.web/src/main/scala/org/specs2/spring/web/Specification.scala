package org.specs2.spring.web

import org.specs2.spring.{TestTransactionDefinitionExtractor, EnvironmentExtractor, JndiEnvironmentSetter}
import org.specs2.spring.TestTransactionDefinitionExtractor.TestTransactionDefinition
import org.springframework.transaction.PlatformTransactionManager
import org.specs2.specification.Example
import org.springframework.mock.web.{MockHttpServletRequest, MockHttpServletResponse}

/**
 * Specification object defining the {{RR}} case class, which carries the
 * {{request}} and an operation that turns the {{request}} into a {{response}}
 */
object Specification {
  case class RR(request: MockHttpServletRequest, op: (MockHttpServletRequest) => MockHttpServletResponse)
}

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
trait Specification extends org.specs2.mutable.Specification {
  import Specification._

  private val testContext = new TestContext()

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

  private def service(method: String) =
    RR(new JspCapableMockHttpServletRequest(method, this.testContext.getDispatcherServlet.getServletConfig), { request: MockHttpServletRequest => doService(request)})

  /**
   * Returns the {{RR}} instance that can be passed to the companion objects that
   * perform the HTTP POST operation
   */
  def post = service("POST")

  /**
   * Returns the {{RR}} instance that can be passed to the companion objects that
   * perform the HTTP GET operation
   */
  def get = service("GET")

  /**
   * Returns the {{RR}} instance that can be passed to the companion objects that
   * perform the HTTP PUT operation
   */
  def put = service("PUT")

  /**
   * Returns the {{RR}} instance that can be passed to the companion objects that
   * perform the HTTP DELETE operation
   */
  def delete = service("DELETE")

  private def doService(request: MockHttpServletRequest) = {
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

      response
    } catch {
      case e: Exception => e.printStackTrace(); throw new RuntimeException(e);
    }
  }

}