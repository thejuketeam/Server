/* NOTE this file is autogenerated by Scalate : see http://scalate.fusesource.org/ */
package src.main.scalate.com.juke.views

import _root_.scala.collection.JavaConversions._
import _root_.org.fusesource.scalate.support.TemplateConversions._
import _root_.org.fusesource.scalate.util.Measurements._

object $_scalate_$SiteIndex_jade {
  def $_scalate_$render($_scalate_$_context: _root_.org.fusesource.scalate.RenderContext): Unit = {
    import _root_.org.fusesource.scalate.support.RenderHelper.{sanitize=>$_scalate_$_sanitize, preserve=>$_scalate_$_preserve, indent=>$_scalate_$_indent, smart_sanitize=>$_scalate_$_smart_sanitize, attributes=>$_scalate_$_attributes}
    ;{
      val helper: xitrum.Action = $_scalate_$_context.attribute("helper")
      import helper._
      ;{
        val context: org.fusesource.scalate.RenderContext = $_scalate_$_context.attribute("context")
        import context._
        
        
        $_scalate_$_context << ( "<p>\n  <img" );
        $_scalate_$_context << $_scalate_$_attributes( $_scalate_$_context, List( (
            "src"
          ,
                        publicUrl("whale.png")

        ) ) )
        $_scalate_$_context << ( "/>\n</p>\n<p>\n  This is a skeleton for a new\n  <a href=\"http://xitrum-framework.github.io/\">Xitrum</a>\n  project.\n</p>\n<p>If you're new to Xitrum, you should visit:</p>\n<ul>\n  <li>\n    <a href=\"http://xitrum-framework.github.io/\">Xitrum Homepage</a>\n  </li>\n  <li>\n    <a href=\"http://xitrum-framework.github.io/guide/\">Xitrum Guide</a>\n  </li>\n</ul>\n<p>\n  This is a skeleton project, you should modify it to suit your needs.\n  Some important parts of the skeleton:\n</p>\n<ul>\n  <li>\n    The program's entry point (\n    <code>main</code>\n    function) is in\n    <a href=\"https://github.com/xitrum-framework/xitrum-new/tree/master/src/main/scala/quickstart/Boot.scala\">src/main/scala/quickstart/Boot.scala</a>\n  </li>\n  <li>\n    Controller actions are in\n    <a href=\"https://github.com/xitrum-framework/xitrum-new/tree/master/src/main/scala/quickstart/action\">src/main/scala/quickstart/action</a>\n    directory.\n  </li>\n  <li>\n    View templates are in\n    <a href=\"https://github.com/xitrum-framework/xitrum-new/tree/master/src/main/scalate/quickstart/action\">src/main/scalate/quickstart/action</a>\n    directory.\n  </li>\n  <li>\n    Configurations are in\n    <a href=\"https://github.com/xitrum-framework/xitrum-new/tree/master/config\">config</a>\n    directory.\n  </li>\n</ul>\n" );
      }
    }
  }
}


class $_scalate_$SiteIndex_jade extends _root_.org.fusesource.scalate.Template {
  def render(context: _root_.org.fusesource.scalate.RenderContext): Unit = $_scalate_$SiteIndex_jade.$_scalate_$render(context)
}
