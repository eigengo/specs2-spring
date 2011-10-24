package org.specs2.spring.web

import org.specs2.web.{WebObjectBodySupport, WebObjectBody}

/**
 * @author janmachacek
 */

trait DispatcherServletDispatcher {
  
  def G(url: String) = {
    new D
  }
  
  class D {
    
    def as[S <: WebObjectBodySupport](s: S) = {
      new ModelAndViewAwareWebObject[S#Body](null, null, s.makeBody(null), None)
    }
    
  }

}