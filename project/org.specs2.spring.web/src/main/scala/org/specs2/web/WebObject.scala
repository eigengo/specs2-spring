package org.specs2.web

import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}
/**
 * Represents processed and pre-chewed HttpServletResponse so that you can write meaningful
 * code in your examples.
 * The instances of {{WebObject}} carry the originating {{request}}, the matching {{response}},
 * together with extracted {{modelAndView}} and a pre-processed {{body}}.
 * The methods {{&lt;&lt;}}, {{&gt;&gt;}} and {{&gt;&gt;!}} manipulate the {{body}}, allowing you to
 * get or set the value of some HTML element, execute arbitrary JavaScript, examine the PDF, ... (depending
 * on what payload processing traits you mixin to your test).
 *
 * @author janmachacek
 */
class WebObject[B <: WebObjectBody](val request: MockHttpServletRequest,
                 val response: MockHttpServletResponse,
                 val bodyOption: Option[B]) {

  /**
   * Returns the payload of the web object's body
   *
   * @return the body of the response
   */
  def ! = body
  
  def body = bodyOption.get


}

/**
 * Models the body of the WebObject--it is the chewed-over response bytes
 */
abstract class WebObjectBody

/**
 * Identifies objects that can modify the {{MockHttpServletRequest}}
 */
trait Requestable {

  /**
   * Implementations must provide a way to apply their bodies to the given {{MockHttpServletRequest}}
   *
   * @param request the HTTP request to be modified
   */
  def request(request: MockHttpServletRequest): MockHttpServletRequest
}

class RequestConstructionException extends Exception
class NoFormException extends RequestConstructionException
class MultipleFormsException extends RequestConstructionException