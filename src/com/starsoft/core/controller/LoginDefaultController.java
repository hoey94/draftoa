package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.mail.MailSender;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.starsoft.core.domain.GroupUsersDomain;
import com.starsoft.core.domain.MenuDomain;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.RoleUsersDomain;
import com.starsoft.core.domain.SystemPropertyDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Role;
import com.starsoft.core.entity.Users;
import com.starsoft.core.exception.NotLoginException;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.WEBCONSTANTS;
/***
 * 登录默认执行页面
 * @author lenovo
 *
 */
public class LoginDefaultController extends BaseAjaxController {
	private MenuDomain menuDomain;
	private OrganDomain organDomain;
	private RoleUsersDomain roleUsersDomain;
	private GroupUsersDomain groupUsersDomain;
	private MailSender mailSender;
	private MailSender qqMailSender;
	private SystemPropertyDomain systemPropertyDomain;
	private UsersDomain usersDomain;
	/**
	 * 默认执行方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.defaultview(request, response);
	}
	/***
	 * 默认登录显示界面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView defaultview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		Users user=(Users)HttpUtil.getLoginUser(request);
		if(user==null){
			throw new NotLoginException();
		}
		List<String> roleIds= (List<String>) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS);
		DetachedCriteria criteria=DetachedCriteria.forClass(Role.class);
		criteria.add(Restrictions.eq("valid", true));
		criteria.add(Restrictions.in("id", roleIds));
		if(roleIds.size()>0){
			List list=this.baseDomain.queryByCriteria(criteria);
			model.put("list", list);
		}else{
			model.put("list", new ArrayList());
		}
		return new ModelAndView("core/users/defaultview", model);
	}
	public void setMenuDomain(MenuDomain menuDomain) {
		this.menuDomain = menuDomain;
	}
	public void setOrganDomain(OrganDomain organDomain) {
		this.organDomain = organDomain;
	}
	public void setRoleUsersDomain(RoleUsersDomain roleUsersDomain) {
		this.roleUsersDomain = roleUsersDomain;
	}
	public void setGroupUsersDomain(GroupUsersDomain groupUsersDomain) {
		this.groupUsersDomain = groupUsersDomain;
	}
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	public void setQqMailSender(MailSender qqMailSender) {
		this.qqMailSender = qqMailSender;
	}
	public void setSystemPropertyDomain(SystemPropertyDomain systemPropertyDomain) {
		this.systemPropertyDomain = systemPropertyDomain;
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
}
