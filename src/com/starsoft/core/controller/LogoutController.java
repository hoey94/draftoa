package com.starsoft.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.starsoft.core.domain.SystemPropertyDomain;
import com.starsoft.core.entity.SystemProperty;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.WEBCONSTANTS;

public class LogoutController extends BaseAjaxController {
	private SystemPropertyDomain systemPropertyDomain;
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String userId=(String) WebUtils.getSessionAttribute(request,WEBCONSTANTS.SESSION_USERID);
		request.getSession().removeAttribute(WEBCONSTANTS.SESSION_USER);
		request.getSession().removeAttribute(WEBCONSTANTS.SESSION_USERNAME);
		request.getSession().removeAttribute(WEBCONSTANTS.SESSION_USERID);
		request.getSession().invalidate();
		ServletContext applation=this.getServletContext();
		List onlineUserlist=(List) applation.getAttribute("onlineUserlist");
		if(onlineUserlist!=null&&onlineUserlist.contains(userId)){
			onlineUserlist.remove(userId);
		}
		String url=HttpUtil.getString(request, "url", request.getHeader("Referer")==null?"/index.do":request.getHeader("Referer"));
//		model.put("url", url);
		return new ModelAndView("login", model);
	}
	/***
	 * 登录显示顶层菜单及个人用户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView loginBottom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		SystemProperty company=(SystemProperty)systemPropertyDomain.query("system.company.name");
		SystemProperty support=(SystemProperty)systemPropertyDomain.query("system.technical.support");
		SystemProperty product=(SystemProperty)systemPropertyDomain.query("system.product.name");
		SystemProperty companyWebSite=(SystemProperty)systemPropertyDomain.query("system.company.website");
		model.put("companyname", company==null?"郑州智星网络科技有限公司":company.getTname());
		model.put("companywebsite", companyWebSite==null?"#":companyWebSite.getTname());
		model.put("supportname", support==null?"郑州智星网络科技有限公司":support.getTname());
		model.put("productname", product==null?"郑州智星网络科技有限公司":product.getTname());
		return new ModelAndView("baseframe/indexBottom", model);
	}
	public void setSystemPropertyDomain(SystemPropertyDomain systemPropertyDomain) {
		this.systemPropertyDomain = systemPropertyDomain;
	}
	
}
