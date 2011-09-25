package org.specs2.spring.web

/**
 * @author janmachacek
 */

trait JavaScriptPayload {
  Payloads.x((contentType, body) => if (contentType == "text/javascript") Some("JS") else None)

}