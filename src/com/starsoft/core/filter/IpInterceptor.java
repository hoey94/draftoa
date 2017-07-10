package com.starsoft.core.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.util.WebUtils;

import com.starsoft.cms.domain.IPBlackListsDomain;
import com.starsoft.cms.entity.IPBlackLists;
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
public class IpInterceptor extends
		org.springframework.web.servlet.handler.HandlerInterceptorAdapter {
	@Autowired
	private IPBlackListsDomain ipBlackListsDomain;
	/**
	 * 访问之前查询查询ip如果在黑名单禁止访问
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		BaseObject loginUser = (BaseObject) WebUtils.getSessionAttribute(
				request, WEBCONSTANTS.SESSION_USER);

		String url = request.getServletPath();
		WebUtils.setSessionAttribute(request, "currenturl", url);
		String   ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
		if (ip == null) {//ip获取不到
				ModelAndView modelAndView = new ModelAndView("login");
				modelAndView.addObject("msg","您的ip地址不对");
				throw new ModelAndViewDefiningException(modelAndView);
			
		} else {
			List<IPBlackLists> ipBlackLists = ipBlackListsDomain.queryByCriteria(ipBlackListsDomain.getCriteria(null).add(Restrictions.eq("ip", ip)));
			if(ipBlackLists.size()>0)
			{
				ModelAndView modelAndView = new ModelAndView("login");
				modelAndView.addObject("msg","您的ip地址被列入黑名单请于管理员联系");
				throw new ModelAndViewDefiningException(modelAndView);
			}
			else{
				return true;
			}
				
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
    }
}