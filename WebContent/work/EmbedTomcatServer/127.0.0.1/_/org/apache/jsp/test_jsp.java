package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class test_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("<html>\r\n");
      out.write("\r\n");
      out.write("<head>\r\n");
      out.write("\t<script type=\"text/javascript\" src=\"javascript/jquery-1.11.2.js\"></script>\r\n");
      out.write("\t<style type=\"text/css\">\r\n");
      out.write("\t\t.main{background-color:red;display:block;text-decoration:none;margin-top:10px;}\r\n");
      out.write("\t\t.name ul{display : none;margin-left:50px;}\r\n");
      out.write("\t\t.name .sh{display:block;}\r\n");
      out.write("\t\t.on{color:red;}\r\n");
      out.write("\t</style>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t<div >\r\n");
      out.write("\t\t<ul class=\"first\">\r\n");
      out.write("\t\t\t<li>第一个</li>\r\n");
      out.write("\t\t\t<li>第二个</li>\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t\t<div class=\"name\">\r\n");
      out.write("\t\t\t<ul class=\"second\">\r\n");
      out.write("\t\t\t\t<li>标签1</li>\r\n");
      out.write("\t\t\t\t<li>标签2</li>\r\n");
      out.write("\t\t\t\t<li>标签3</li>\r\n");
      out.write("\t\t\t</ul>\r\n");
      out.write("\t\t\t<ul class=\"second\">\r\n");
      out.write("\t\t\t\t<li>标签1</li>\r\n");
      out.write("\t\t\t\t<li>标签2</li>\r\n");
      out.write("\t\t\t\t<li>标签3</li>\r\n");
      out.write("\t\t\t</ul>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("</body>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("\t$(\".name\").children(\"ul\").siblings().removeClass(\"sh\").eq(localStoreage.one).addClass(\"sh\");\r\n");
      out.write("\t$(\".name ul\").children(\"li\").siblings().removeClass(\"on\").eq(localStoreage.two).addClass(\"on\");\r\n");
      out.write("\t$(\".main ul li\").click(function(){\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar _index = $(this).index();\r\n");
      out.write("\t\t$(\".name\").children(\"ul\").siblings().removeClass(\"sh\").eq(_index).addClass(\"sh\");\r\n");
      out.write("\t\tlocalStorage.one = _index;\r\n");
      out.write("\t\talert(_index);\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t$(\".name ul li\").click(function(){\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\tvar index = $(this).index();\r\n");
      out.write("\t\t\t$(\".name ul li\").eq(index).addClass(\"on\");\r\n");
      out.write("\t\t\tlocalStoreage.two = index;\r\n");
      out.write("\t\t\talert(index);\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
