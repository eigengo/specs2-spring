package org.specs2.spring.web

/**
 * @author janmachacek
 */

trait JavaScriptPayload extends PayloadRegistryAccess {
  x((contentType, body) => if (contentType == "text/javascript") Some("JS") else None)

}