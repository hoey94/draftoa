package com.starsoft.oa.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.starsoft.core.entity.Users;
import com.starsoft.core.filter.LoginInterceptor;

public class MyLoginInterceptor extends LoginInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		if(user == null){
			throw new ModelAndViewDefiningException(new ModelAndView("oa/info/infoLogin"));
		}
		return true;
	}

	
	
}
