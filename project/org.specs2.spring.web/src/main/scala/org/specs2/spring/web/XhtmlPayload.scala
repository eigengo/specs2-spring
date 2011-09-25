package org.specs2.spring.web

/**
 * @author janmachacek
 */

trait XhtmlPayload extends PayloadRegistryAccess {
  x((contentType, body) => if (contentType == "text/html;charset=UTF-8") Some("XHTML") else None)

}