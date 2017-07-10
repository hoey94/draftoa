package com.starsoft.core.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.util.WebUtils;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.SystemLog;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.WEBCONSTANTS;
/****
 * 网站前端信息拦截器
 * @author lenovo
 *
 */
public class WebLoginInterceptor extends
		org.springframework.web.servlet.handler.HandlerInterceptorAdapter {
	public final Logger logger = LogManager.getLogger(LoginInterceptor.class);

	/**
	 * 访问之前查询
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		BaseObject loginUser = (BaseObject) WebUtils.getSessionAttribute(
				request, WEBCONSTANTS.SESSION_USER);
		String url = request.getServletPath();
		WebUtils.setSessionAttribute(request, "currenturl", url);
		if (loginUser == null) {
			String action=HttpUtil.getString(request, "action", "");
			if(!action.equals("")&&action.equals("commonselectlist")){
				return true;
			}else{
				String query = request.getQueryString();
				ModelAndView modelAndView = new ModelAndView("html/notlogin");
				if (query != null) {
					modelAndView
							.addObject("signonForwardAction", url + "?" + query);
				} else {
					modelAndView.addObject("signonForwardAction", url);
				}
				throw new ModelAndViewDefiningException(modelAndView);
			}
		} else {
			boolean hasright = true;
			if (hasright) {// 有操作权限
				return true;
			} else {// 没有操作权限
				ModelAndView modelAndView = new ModelAndView(
						"common/messageerror");
				throw new ModelAndViewDefiningException(modelAndView);
			}
		}
	}

//	/***
//	 * 登记日志记录
//	 */
//	public void postHandle(HttpServletRequest request,
//			HttpServletResponse response, Object obj, ModelAndView mv)
//			throws Exception {
//		if (logger.isInfoEnabled()) {
//			Users loginUser = (Users) WebUtils.getSessionAttribute(request,
//					WEBCONSTANTS.SESSION_USER);
//			LogInfo logInfo = new LogInfo();
//			String path = request.getRequestURI();
//			String query = request.getQueryString();
//			if(query!=null&&query.equals("")){
//				path += "?" + query;
//			}
//			if (loginUser != null) {
//				logInfo.setTname("登录成功日志");
//				logInfo.setCreateId(loginUser.getId());
//			} else {
//				logInfo.setTname("登录失败，用户不存在");
//			}
//	       	logInfo.setIp(request.getRemoteAddr());
//	       	logInfo.setInfoClass(logger.getName());
//	       	logInfo.setQpath(path);
//   	        logInfo.setInfoContent("系统访问日志");
//   	        logInfoDomain.save(logInfo);
//		}
		
//	}
	
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
    }
}