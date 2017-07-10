package com.starsoft.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateQueryException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import com.starsoft.core.domain.SystemLogDomain;
import com.starsoft.core.entity.SystemLog;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.WEBCONSTANTS;
/***
 * 异常统一处理类
 * @author lenovo
 *
 */
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
	private SystemLogDomain systemLogDomain;
	public final Logger logger = LogManager.getLogger(MyHandlerExceptionResolver.class);
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		Map<String,String> model = new HashMap<String,String>();
		model.put("msg", ex.getMessage());
		try {
			StringBuffer detailmsg = new StringBuffer("");  
			if (ex != null) {
				int length = ex.getStackTrace().length;//行号
				for (int i = 0; i < length; i++) {
					detailmsg.append("\t" + ex.getStackTrace()[i] + "\n");
					if(i>30) break;
				}
			}
			model.put("detailmsg", detailmsg.toString());
			if(handler!=null){
				model.put("ex", handler.getClass().getName());
				this.saveLog(request,handler.getClass().getName(),ex.toString(),detailmsg.toString());
			}else{
				model.put("ex", "无控制器错误");
				this.saveLog(request,"无控制器错误",ex.toString(),detailmsg.toString());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		if (ex instanceof NotLoginException) {
			return new ModelAndView("login", model);
		} else if (ex instanceof OperateException) {
			return new ModelAndView("common/exception",model);
		}else if (ex instanceof HibernateQueryException) {
			model.put("msg","数据库操作失败!");
			return new ModelAndView("common/exception",model);
		}else if (ex instanceof Exception) {
			model.put("msg","系统操作失败!");
			return new ModelAndView("common/exception",model);
		}
		return null;
	}
	/***
	 * 保存错误日志记录
	 * @param request
	 * @param ex
	 * @throws Exception
	 */
	public void saveLog(HttpServletRequest request,String className,String exceptionClass,String detailmsg) throws Exception{
		Users loginUser = (Users) WebUtils.getSessionAttribute(request,
				WEBCONSTANTS.SESSION_USER);
		SystemLog systemLog = new SystemLog();
		String path = request.getRequestURI();
		String query = request.getQueryString();
		if(query!=null&&query.equals("")){
			path += "?" + query;
		}
		systemLog.setTname("访问出现异常");
		if (loginUser != null) {
			systemLog.setCreateId(loginUser.getId());
		}
		if(exceptionClass!=null&&exceptionClass.length()>200){
			systemLog.setExceptionClass(exceptionClass.substring(0,199));
		}
		systemLog.setBrowserType(HttpUtil.getBrowserType(request));
		systemLog.setIp(request.getRemoteAddr());
		systemLog.setInfoClass(className);
		systemLog.setQpath(path);
		systemLog.setInfoContent(detailmsg);
		systemLogDomain.save(systemLog);
	}
	public void setSystemLogDomain(SystemLogDomain systemLogDomain) {
		this.systemLogDomain = systemLogDomain;
	}

}
