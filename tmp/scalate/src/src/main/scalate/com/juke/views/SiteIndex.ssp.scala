/* NOTE this file is autogenerated by Scalate : see http://scalate.fusesource.org/ */
package src.main.scalate.com.juke.views

import _root_.scala.collection.JavaConversions._
import _root_.org.fusesource.scalate.support.TemplateConversions._
import _root_.org.fusesource.scalate.util.Measurements._

object $_scalate_$SiteIndex_ssp {
  def $_scalate_$render($_scalate_$_context: _root_.org.fusesource.scalate.RenderContext): Unit = {
    ;{
      val helper: xitrum.Action = $_scalate_$_context.attribute("helper")
      import helper._
      ;{
        val context: org.fusesource.scalate.RenderContext = $_scalate_$_context.attribute("context")
        import context._
        
        
        import         com.juke.views._

        $_scalate_$_context << ( "\n\n<html>\n\t<head>\n\t\t<title> JUKE </title>\n\t\t<link type=\"text/css\" rel=\"stylesheet\" media=\"all\" href=" );
        $_scalate_$_context <<< (         webJarsUrl("bootstrap/3.2.0/css", "bootstrap.css", "bootstrap.min.css")
 );
        $_scalate_$_context << ( " />\n\t\t<link type=\"text/css\" rel=\"stylesheet\" media=\"all\" href=" );
        $_scalate_$_context <<< (         publicUrl("css/main_page.css")
 );
        $_scalate_$_context << ( " />\n\t</head>\n\t<body>\n\t    <main>\n        \t<!--Add as many sections as necessary -->\n\t\t\t<section id=\"slide-1\" class=\"homeSlide\">\n            \t<div class=\"bcg\" data-center=\"background-position: 50% 0px;\" data-top-bottom=\"background-position: 50% -100px;\" data-anchor-target=\"#slide-1\">\n\t\t\t\t\t<div class=\"hsContainer\">\n                    \t<div class=\"hsContent\" data-center=\"bottom: 200px; opacity: 1\" data-top=\"bottom:1200px; opacity: 0\" data-anchor-target=\"#slide-1 h2\">\n                        \t<h2>Simple parallax scrolling is...</h2>\n                    \t</div>\n                \t</div>\n            \t</div>\n        \t</section>\n\n\t\t\t<section id=\"slide-2\" class=\"homeSlide\">\n            \t<div class=\"bcg\" data-center=\"background-position: 50% 0px;\" data-top-bottom=\"background-position: 50% -100px;\" data-bottom-top=\"background-position: 50% 100px\" data-anchor-target=\"#slide-2\">\n                \t<div class=\"hsContainer\">\n                    \t<div class=\"hsContent\" data-center=\"bottom: 200px; opacity: 1\" data-top=\"bottom:1200px; opacity: 0\" data-anchor-target=\"#slide-2 h2\">\n                        \t<h2>Simple parallax scrolling is...</h2>\n                    \t</div>\n                \t</div>\n            \t</div>\n        \t</section>\n\n\n\t\t\t<section id=\"slide-3\" class=\"homeSlide\">\n            \t<div class=\"bcg\" data-center=\"background-position: 50% 0px;\" data-top-bottom=\"background-position: 50% -100px;\" data-bottom-top=\"background-position: 50% 100px\" data-anchor-target=\"#slide-3\">\n                \t<div class=\"hsContainer\">\n                    \t<div class=\"hsContent\" \n\t\t\t                \tdata--50-bottom=\"opacity: 0;\"\n        \t\t\t        \tdata--200-bottom=\"opacity: 1;\"\n        \t\t\t        \tdata-center=\"opacity: 1\"\n        \t\t\t        \tdata-200-top=\"opacity: 0\" \n\t\t\t\t\t\t\t\tdata-anchor-target=\"#slide-3 h2\">\n                        \t<h2>Simple parallax scrolling is...</h2>\n                    \t</div>\n                \t</div>\n            \t</div>\n        \t</section>\n\t\n\t\t\t<section id=\"slide-4\" class=\"homeSlide\">\n            \t<div class=\"bcg\" data-center=\"background-position: 50% 0px;\" data-top-bottom=\"background-position: 50% -100px;\" data-bottom-top=\"background-position: 50% 100px\" data-anchor-target=\"#slide-4\">\n                \t<div class=\"hsContainer\">\n                    \t<div class=\"hsContent\" \n                        \t    data-bottom-top=\"opacity: 0\"\n\t\t\t\t                data-25p-top=\"opacity: 0\"\n                \t\t\t\tdata-top=\"opacity: 1\" \n                \t\t\t\tdata-anchor-target=\"#slide-4\">\n\t\t\t\t\t\t\t<h2>Simple parallax scrolling is...</h2>\n                    \t</div>\n                \t</div>\n            \t</div>\n        \t</section>\t\t\n\n\t\t</main>\n\t\t\n\t\t<script type=\"text/javascript\" src=" );
        $_scalate_$_context <<< (         publicUrl("js/jquery-2.1.1.min.js")
 );
        $_scalate_$_context << ( "></script>\n\t\t<script type=\"text/javascript\" src=" );
        $_scalate_$_context <<< (         publicUrl("js/skrollr.js")
 );
        $_scalate_$_context << ( "></script>\n\t\t<script type=\"text/javascript\" src=" );
        $_scalate_$_context <<< (         publicUrl("js/main_page.js")
 );
        $_scalate_$_context << ( "></script>\n\n\t</body>\n</html>\n" );
      }
    }
  }
}


class $_scalate_$SiteIndex_ssp extends _root_.org.fusesource.scalate.Template {
  def render(context: _root_.org.fusesource.scalate.RenderContext): Unit = $_scalate_$SiteIndex_ssp.$_scalate_$render(context)
}