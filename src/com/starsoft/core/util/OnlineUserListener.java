package com.starsoft.core.util;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class OnlineUserListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		
	}
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session=event.getSession();
		ServletContext applation=session.getServletContext();
		String userId=(String) session.getAttribute(WEBCONSTANTS.SESSION_USERID);
		List onlineUserlist=(List) applation.getAttribute("onlineUserlist");
		if(onlineUserlist!=null&&onlineUserlist.contains(userId)){
			onlineUserlist.remove(userId);
			System.out.println("超时退出用户："+userId);
		}
	}

}
