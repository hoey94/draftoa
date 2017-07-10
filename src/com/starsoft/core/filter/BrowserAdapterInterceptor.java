package com.starsoft.core.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.starsoft.core.util.HttpUtil;
/***
 * 浏览器视图过滤器，主要功能拦截关于视图方面的信息
 * @author lenovo
 *
 */
public class BrowserAdapterInterceptor extends org.springframework.web.servlet.handler.HandlerInterceptorAdapter {
	public final Logger logger = LogManager.getLogger(BrowserAdapterInterceptor.class);
	/***
    *可以做请求后的返回视图的处理，判断是否为移动客户端，根据不同的客户端返回不同的视图
    */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mv) throws Exception {
    		if(mv!=null){//请求的方式不是ajax方式
    			if(false){//如果是移动客户端，修改返回试图路径
        			if(!mv.getViewName().endsWith("TV")){
        	    		mv.setViewName(mv.getViewName()+"TV");
        			}
        		}else{
//        			logger.info("================================"+mv.getViewName());
        		}
    		}
	    }
}
