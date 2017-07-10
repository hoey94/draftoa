package com.starsoft.core.filter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.util.WebUtils;

import com.starsoft.core.domain.AppesActionPrivilegeDomain;
import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.domain.SystemLogDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.SystemLog;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.WEBCONSTANTS;

public class LoginInterceptor extends org.springframework.web.servlet.handler.HandlerInterceptorAdapter {
   private SystemLogDomain systemLogDomain;
   private AppesActionPrivilegeDomain appesActionPrivilegeDomain;
   private AppesDomain appesDomain;
   public final Logger logger = LogManager.getLogger(LoginInterceptor.class);
  /**
   * 访问之前验证登录权限,访问之后检查所做的操作记录
   */
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception 	{
	   Users loginUser = (Users) WebUtils.getSessionAttribute(request,
			   WEBCONSTANTS.SESSION_USER);
	    String url = request.getServletPath();
	    WebUtils.setSessionAttribute(request, "currenturl", url);
	    String action=HttpUtil.getString(request, "action","list");
   		if(loginUser ==null){
   			String query = request.getQueryString();

   			// 未登录者跳转的页面路径
   			ModelAndView modelAndView = new ModelAndView("oa/info/infologin");
//   		ModelAndView modelAndView = new ModelAndView("login");
   			if(query!=null){
   				modelAndView.addObject("signonForwardAction",url+"?"+query);
   			}else{
   				modelAndView.addObject("signonForwardAction",url);	
   			}
   			String requestType = request.getHeader("X-Requested-With");  
   			if("XMLHttpRequest".equals(requestType)){
   				HttpResponseResult resultBean=new HttpResponseResult(HttpResponseResult.FAIL,"请先登陆","login.do");
   				request.setAttribute("lastAjaxActionResult", HttpResponseResult.FAIL);
   				response.setContentType("application/json;charset=UTF-8");   
   		        try {   
   		            PrintWriter out = response.getWriter();   
   		            out.write(JSONObject.fromObject(resultBean).toString()); 
   		            out.close();
   		        } catch (IOException e) {   
   		            e.printStackTrace();   
   		        }      
   		        return false;
   			}else{
   				throw new ModelAndViewDefiningException(modelAndView);
   			}
   			
   		}else{ 
   			return true;
   			//进行业务操作权限验证
//   			List<String> roleIds=loginUser.getRoles();
//   			if(url.indexOf(".do")>0){
//   				boolean result=false;
//   				if(roleIds.size()>0){
//   					String appes=url.substring(0, url.indexOf(".do")).trim();
//   					if(appes.startsWith("/")){
//   						appes=appes.substring(1, appes.length());
//   					}
//   					Appes app=appesDomain.getAppes(appes);
//   					if(app!=null&&app.getActionList().size()>0){
//   						result = appesActionPrivilegeDomain.hasPrivilege(appes, action, roleIds);
//   					}else{
//   						result = true;
//   					}
//   	   			}else{
//   	   				result = true;
//   	   			}
//   				if(result){
//   					return true;
//   				}else{
//   					String query = request.getQueryString();
//   		   			ModelAndView modelAndView = new ModelAndView("login");
//   		   			if(query!=null){
//   		   				modelAndView.addObject("signonForwardAction",url+"?"+query);
//   		   			}else{
//   		   				modelAndView.addObject("signonForwardAction",url);	
//   		   			}
//	   				modelAndView.addObject("msg","您不具备应用级别操作权限，请联系管理员");
//   		   			throw new ModelAndViewDefiningException(modelAndView);
//   				}
//   			}else{
//   				return true;
//   			}
   		}
   }
    /***
     * 视图正常返回后执行的操作，可以做日志相关开发
     */
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex)throws Exception { 
    	Users loginUser = HttpUtil.getLoginUser(request);
		SystemLog systemLog = new SystemLog();
		String path = request.getRequestURI();
		String query=request.getQueryString();
		String action=HttpUtil.getString(request, "action","");
		String id=HttpUtil.getString(request, "id", "");
		String ids=HttpUtil.getString(request, "ids", "");
		if(action.startsWith("save")||action.startsWith("update")||action.startsWith("delete")||action.startsWith("jsdelete")){
			if(query!=null&&query.equals("")){
				path += "?" + query;
			}
	       	if(path.indexOf("logInfo.do")<0){
	       		if(loginUser!=null){
	       			systemLog.setCreateId(loginUser.getId());
	       		}
	       		systemLog.setTname("数据变更");
	       		systemLog.setIp(request.getRemoteAddr());
	       		systemLog.setInfoClass(handler.getClass().getName());
	       		systemLog.setQpath(path);
	       		systemLog.setBrowserType(HttpUtil.getBrowserType(request));
	       		systemLog.setInfoContent("调用了方法"+action+",操作的对象："+id+ids);
	       		systemLogDomain.save(systemLog);
	       	}
		}
    }
	public void setAppesActionPrivilegeDomain(
			AppesActionPrivilegeDomain appesActionPrivilegeDomain) {
		this.appesActionPrivilegeDomain = appesActionPrivilegeDomain;
	}
	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}
	public void setSystemLogDomain(SystemLogDomain systemLogDomain) {
		this.systemLogDomain = systemLogDomain;
	}
  
}