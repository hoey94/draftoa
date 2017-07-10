package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<html>\r\n");
      out.write("<title>——电子提案系统!</title>\r\n");
      out.write("<meta name=\"Keywords\" content=\"电子提案系统|电子提案系统\" />\r\n");
      out.write("<meta name=\"Description\" content=\"电子提案系统，电子提案系统。\" />\r\n");
      out.write("<head>\r\n");
      out.write("<script type=\"text/javascript\">  \r\n");
      out.write("//平台、设备和操作系统   \r\n");
      out.write("var system ={  \r\n");
      out.write("\r\n");
      out.write("    win : false,  \r\n");
      out.write("\r\n");
      out.write("    mac : false,  \r\n");
      out.write("\r\n");
      out.write("    xll : false  \r\n");
      out.write("\r\n");
      out.write("};  \r\n");
      out.write("//检测平台   \r\n");
      out.write("var p = navigator.platform;  \r\n");
      out.write("system.win = p.indexOf(\"Win\") == 0;  \r\n");
      out.write("\r\n");
      out.write("system.mac = p.indexOf(\"Mac\") == 0;  \r\n");
      out.write("\r\n");
      out.write("system.x11 = (p == \"X11\") || (p.indexOf(\"Linux\") == 0);  \r\n");
      out.write("//跳转语句   \r\n");
      out.write("\r\n");
      out.write("if(system.win||system.mac||system.xll){//转向后台登陆页面  \r\n");
      out.write("    window.location.href=\"index.do\";  \r\n");
      out.write("}else{  \r\n");
      out.write("   /*  window.location.href=\"mobileIndex.do\";   */\r\n");
      out.write("\twindow.location.href=\"index.do\";\r\n");
      out.write("}  \r\n");
      out.write("</script>  \r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
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
