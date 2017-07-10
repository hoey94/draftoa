package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.starsoft.core.domain.DelegationAuthorizationDomain;
import com.starsoft.core.domain.GroupUsersDomain;
import com.starsoft.core.domain.MenuDomain;
import com.starsoft.core.domain.MenuPrivilegeDomain;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.RoleDomain;
import com.starsoft.core.domain.RoleUsersDomain;
import com.starsoft.core.domain.SystemPropertyDomain;
import com.starsoft.core.domain.SystemPropertyPersonDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.Fans;
import com.starsoft.core.entity.FavouritesLink;
import com.starsoft.core.entity.LogInRecord;
import com.starsoft.core.entity.Menu;
import com.starsoft.core.entity.MsgInfo;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.Reminds;
import com.starsoft.core.entity.Role;
import com.starsoft.core.entity.RoleUsers;
import com.starsoft.core.entity.SystemProperty;
import com.starsoft.core.entity.SystemPropertyPerson;
import com.starsoft.core.entity.Users;
import com.starsoft.core.entity.WaitToRead;
import com.starsoft.core.exception.NotLoginException;
import com.starsoft.core.task.MailSenderTask;
import com.starsoft.core.util.HashUtil;
import com.starsoft.core.util.HttpSendUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.Pinyin4jUtil;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;

public class LoginController extends BaseAjaxController {
	private MenuDomain menuDomain;
	private OrganDomain organDomain;
	private RoleUsersDomain roleUsersDomain;
	private GroupUsersDomain groupUsersDomain;
	private SystemPropertyDomain systemPropertyDomain;
	private SystemPropertyPersonDomain systemPropertyPersonDomain;
	private UsersDomain usersDomain;
	private MailSenderTask mailSenderTask;
	private DelegationAuthorizationDomain delegationAuthorizationDomain;
	private MenuPrivilegeDomain menuPrivilegeDomain;
	private RoleDomain roleDomain;
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String url=HttpUtil.getString(request, "url", request.getHeader("Referer")==null?"/index.do":request.getHeader("Referer"));
		model.put("url", url);
		String id=HttpUtil.getString(request, "id","");
		String passwords=HttpUtil.getString(request, "password","");
		String ipForget=HttpUtil.getString(request, "ipForget","");
//		SystemProperty company=(SystemProperty)systemPropertyDomain.query("system.company.name");
//		SystemProperty support=(SystemProperty)systemPropertyDomain.query("system.technical.support");
//		SystemProperty product=(SystemProperty)systemPropertyDomain.query("system.product.name");
//		model.put("companyname", company==null?"单位名称":company.getTname());
//		model.put("supportname", support==null?"技术支持名称":support.getTname());
//		model.put("productname", product==null?"产品名称":product.getTname());
		if(id.equals("")||passwords.equals("")){
			String msg="帐号或者密码为空!";
			return new ModelAndView("login", model);
		}
//		String txtyzm=HttpUtil.getString(request, "txtyzm","");
//		String sessionyzm=(String) request.getSession().getAttribute("verifycode");
//		if(!txtyzm.equals(sessionyzm)){
//			String msg="验证码错误";
//			model.put("msg", msg);
//			model.put("id", id);
//			model.put("password", passwords);
//			return new ModelAndView("login", model);
//		}
		Users user=(Users)usersDomain.query(id);
		if(user==null){
			if(StringUtil.isMobileNumber(id)){//手机号码输入
				user=(Users)usersDomain.queryFirstByProperty("mobilNumber", id);
			}
			if(user==null&&StringUtil.isEmail(id)){
				user=(Users)usersDomain.queryFirstByProperty("email", id);
			}
			if(user==null&&StringUtil.isNumber(id)){//qq帐号登录
				user=(Users)usersDomain.queryFirstByProperty("qqNumber", id);
			}
			if(user==null){
				model.put("msg", "帐号不存在!请重新输入");
				return new ModelAndView("login", model);
			}
		}
		String password=HashUtil.toMD5(passwords);
		if(!user.isValid()){
			model.put("msg", "帐号已经禁用!请联系管理员");
			return new ModelAndView("login", model);
		}
		if(!password.equals(user.getPassword())){
			model.put("msg", "密码不正确!请重新输入");
			model.put("id", id);
			String msg="用户名或密码不对";
			//登录失败记录
			LogInRecord logInRecord=new LogInRecord();
			logInRecord.setCreateId(id);
			logInRecord.setTname(msg);
			logInRecord.setValid(false);
			logInRecord.setIp(request.getRemoteAddr());
			logInRecord.setBrowserType(HttpUtil.getBrowserType(request));
			this.baseDomain.save(logInRecord);
			model.put("id", id);
			return new ModelAndView("login", model);
		}
//		System.out.println("====================ipForget="+ipForget);
		//在线人数控制
		List<String> roleIds=this.roleUsersDomain.getRoleIdsByUserId(user.getId());
		//增加授权人角色
//		List<String> delegationRoleIds = delegationAuthorizationDomain.queryRoleIdsByUserId(user.getId());
//		roleIds.addAll(delegationRoleIds);
		user.setRoles(roleIds);
		List<String> groupIds=this.groupUsersDomain.getGroupIdsByUserId(user.getId());
		user.setGroups(groupIds);
		WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USER, user);
		WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USERNAME, user.getTname());
		WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USERID, user.getId());
		WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USERTYPE, user.getDuty());
		boolean flag=false;
		if("admin".equals(id)){
			flag=true;
		}
		WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS,roleIds);
		WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEFLAG,flag);
		LogInRecord logInRecord=new LogInRecord();
		logInRecord.setCreateId(id);
		logInRecord.setTname("登录成功");
		logInRecord.setValid(true);
		logInRecord.setBrowserType(HttpUtil.getBrowserType(request));
		logInRecord.setIp(request.getRemoteAddr());
		this.baseDomain.save(logInRecord);
		//查询允许个性化信息属性设置，放置在session
		List<SystemProperty> systemPropertyList=systemPropertyDomain.queryByCriteria(systemPropertyDomain.getCriteria(true));
		for(SystemProperty systemProperty:systemPropertyList){
			String systemPropertyId=systemProperty.getId();
			String systemPropertyValue=systemProperty.getTname();
			WebUtils.setSessionAttribute(request,systemPropertyId,systemPropertyValue);
		}
		List<SystemPropertyPerson> systemPropertyPersonList=systemPropertyPersonDomain.queryByProperty("createId", id);
		for(SystemPropertyPerson systemPropertyPerson:systemPropertyPersonList){
			String systemPropertyId=systemPropertyPerson.getSystemPropertyId();
			String systemPropertyValue=systemPropertyPerson.getTname();
			WebUtils.setSessionAttribute(request,systemPropertyId,systemPropertyValue);
		}
		List<Menu> topList=new ArrayList<Menu>();
		topList=menuDomain.queryByParentId(rootValue, true, false);
		List resultList=new ArrayList();
		for(Menu menu:topList){
			boolean result=menuPrivilegeDomain.queryPrivilegeByMenuIdAndRoleIds(menu.getId(), roleIds);
			if(result){
				resultList.add(menu);
			}
		}
		WebUtils.setSessionAttribute(request, "topList", resultList);
		List menulist=new ArrayList();
		String parentId=HttpUtil.getString(request,"parentId", "myhome");
		if(user.getId().equals("admin")){
			menulist=menuDomain.queryTreeByParentId(parentId, 3, true, false);
		}else{
			menulist=menuDomain.querySubTreesByParentIdAndRoles(parentId, roleIds);
		}
		WebUtils.setSessionAttribute(request, "menulist", menulist);
		Menu mainMenu=(Menu)menuDomain.query("myhome");
		WebUtils.setSessionAttribute(request, "mainMenu", mainMenu);
		//我的私信
		DetachedCriteria sxcriteria=DetachedCriteria.forClass(MsgInfo.class);
		Page sxpage=new Page(0,20);
		sxcriteria.add(Restrictions.eq("receiverId", id));
		sxcriteria.addOrder(Order.desc("id"));
		sxcriteria.add(Restrictions.eq("valid",true));//未读
		List sxlist=this.baseDomain.queryByCriteria(sxcriteria, sxpage);
		WebUtils.setSessionAttribute(request, "sxlist", sxlist);
		WebUtils.setSessionAttribute(request, "sxsize", sxpage.getTotalCount());
		//站内通知
		DetachedCriteria znxcriteria=DetachedCriteria.forClass(WaitToRead.class);
		Page znxpage=new Page(0,5);
		znxcriteria.add(Restrictions.eq("transactId", id));
		znxcriteria.addOrder(Order.desc("id"));
		znxcriteria.add(Restrictions.eq("valid",false));//未读
		List znxlist=this.baseDomain.queryByCriteria(znxcriteria, znxpage);
		WebUtils.setSessionAttribute(request, "znxlist", znxlist);
		WebUtils.setSessionAttribute(request, "znxsize", znxlist.size());
		//我的粉丝
		DetachedCriteria fscriteria=DetachedCriteria.forClass(Fans.class);
		Page fspage=new Page(0,20);
		fscriteria.add(Restrictions.eq("fansId", id));
		fscriteria.addOrder(Order.desc("id"));
		fscriteria.add(Restrictions.eq("valid",true));//未读
		List fslist=this.baseDomain.queryByCriteria(fscriteria, fspage);
		WebUtils.setSessionAttribute(request, "fslist", fslist);
		WebUtils.setSessionAttribute(request, "fssize", fspage.getTotalCount());
		//我的提醒
		DetachedCriteria txcriteria=DetachedCriteria.forClass(Reminds.class);
		Page txpage=new Page(0,20);
		txcriteria.add(Restrictions.eq("receiverId", id));
		txcriteria.addOrder(Order.desc("id"));
		txcriteria.add(Restrictions.eq("valid",true));//未读
		List txlist=this.baseDomain.queryByCriteria(txcriteria, txpage);
		WebUtils.setSessionAttribute(request, "txlist", txlist);
		WebUtils.setSessionAttribute(request, "txsize", txpage.getTotalCount());
		//查询自己学校的logo
		Organ organ=(Organ)this.organDomain.query(user.getOrganId());
		if(organ!=null){
			String suborganlogo=organ.getImageurl();
			String schoolimage=organ.getSchoolimage();
			if(!StringUtil.isNullOrEmpty(schoolimage)){
				WebUtils.setSessionAttribute(request, "schoolimage", schoolimage);
			}
			if(!StringUtil.isNullOrEmpty(suborganlogo)){
				WebUtils.setSessionAttribute(request, "suborganlogo", suborganlogo);
			}
			if(!StringUtil.isNullOrEmpty(organ.getOrganCode())){
				WebUtils.setSessionAttribute(request, "suborgancode", organ.getOrganCode());
			}
			WebUtils.setSessionAttribute(request, "suborganname", organ.getTname());
			//查询课程数量 微课程数量
			String wksql="select count(t.id) from t_edu_lesson as t left join t_edu_course as tc on t.courseId=tc.id where tc.organId=?";
			//查询用户数量
			String yhsql="select count(id) from t_core_user where organId=?";
			//查询课程数量
			String kcsql="select count(id) from t_edu_course where organId=?";
			int wkcount=0;
			int yhcount=10;
			int kccount=5;
//			int kccount=this.jdbcTemplate.queryForInt(kcsql,new Object[]{user.getOrganId()});
			WebUtils.setSessionAttribute(request,"wkcountu", wkcount);
			WebUtils.setSessionAttribute(request,"yhcountu", yhcount);
			WebUtils.setSessionAttribute(request,"kccountu", kccount);
		}
		model.put("obj", user);
		if(!StringUtil.isNullOrEmpty(url)&&url.indexOf("logout.do")<0&&url.indexOf("login.do")<0){
//			return new ModelAndView("redirect:"+url, model);
			return new ModelAndView("tpa/indexwx", model);
		}else{
			return new ModelAndView("redirect:/index.do", model);
		}
	}
	public void ajax(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id","");
		String passwords=HttpUtil.getString(request, "password","");
		if(id.equals("")||passwords.equals("")){
			String msg="帐号或者密码为空!";
			this.outFailString(request, response, msg, "");
		}else{
			Users user=(Users)usersDomain.query(id);
			if(user==null){
				if(StringUtil.isMobileNumber(id)){//手机号码输入
					user=(Users)usersDomain.queryFirstByProperty("mobilNumber", id);
				}
				if(user==null&&StringUtil.isEmail(id)){
					user=(Users)usersDomain.queryFirstByProperty("email", id);
				}
				if(user==null&&StringUtil.isNumber(id)){//qq帐号登录
					user=(Users)usersDomain.queryFirstByProperty("qqNumber", id);
				}
				if(user==null){
					this.outFailString(request, response, "帐号不存在!请重新输入", "");
				}
			}
			String password=HashUtil.toMD5(passwords);
			if(!user.isValid()){
				this.outFailString(request, response, "帐号已经禁用!请联系管理员", "");
			}
			if(!password.equals(user.getPassword())){
				String msg="用户名或密码不对";
				//登录失败记录
				LogInRecord logInRecord=new LogInRecord();
				logInRecord.setCreateId(id);
				logInRecord.setTname(msg);
				logInRecord.setValid(false);
				logInRecord.setIp(request.getRemoteAddr());
				logInRecord.setBrowserType(HttpUtil.getBrowserType(request));
				this.baseDomain.save(logInRecord);
				this.outFailString(request, response, msg, "");
			}else{
				//在线人数控制
				List<String> roleIds=this.roleUsersDomain.getRoleIdsByUserId(user.getId());
				//增加授权人角色
				//		List<String> delegationRoleIds = delegationAuthorizationDomain.queryRoleIdsByUserId(user.getId());
				//		roleIds.addAll(delegationRoleIds);
				user.setRoles(roleIds);
				List<String> groupIds=this.groupUsersDomain.getGroupIdsByUserId(user.getId());
				user.setGroups(groupIds);
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USER, user);
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USERNAME, user.getTname());
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USERID, user.getId());
				boolean flag=false;
				if("admin".equals(id)){
					flag=true;
				}
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS,roleIds);
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEFLAG,flag);
				LogInRecord logInRecord=new LogInRecord();
				logInRecord.setCreateId(id);
				logInRecord.setTname("登录成功");
				logInRecord.setValid(true);
				logInRecord.setBrowserType(HttpUtil.getBrowserType(request));
				logInRecord.setIp(request.getRemoteAddr());
				this.baseDomain.save(logInRecord);
				//查询允许个性化信息属性设置，放置在session
				List<SystemProperty> systemPropertyList=systemPropertyDomain.queryByCriteria(systemPropertyDomain.getCriteria(true));
				for(SystemProperty systemProperty:systemPropertyList){
					String systemPropertyId=systemProperty.getId();
					String systemPropertyValue=systemProperty.getTname();
					WebUtils.setSessionAttribute(request,systemPropertyId,systemPropertyValue);
				}
				List<SystemPropertyPerson> systemPropertyPersonList=systemPropertyPersonDomain.queryByProperty("createId", id);
				for(SystemPropertyPerson systemPropertyPerson:systemPropertyPersonList){
					String systemPropertyId=systemPropertyPerson.getSystemPropertyId();
					String systemPropertyValue=systemPropertyPerson.getTname();
					WebUtils.setSessionAttribute(request,systemPropertyId,systemPropertyValue);
				}
				List<Menu> topList=new ArrayList<Menu>();
				topList=menuDomain.queryByParentId(rootValue, true, false);
				List resultList=new ArrayList();
				for(Menu menu:topList){
					boolean result=menuPrivilegeDomain.queryPrivilegeByMenuIdAndRoleIds(menu.getId(), roleIds);
					if(result){
						resultList.add(menu);
					}
				}
				WebUtils.setSessionAttribute(request, "topList", resultList);
				List menulist=new ArrayList();
				String parentId=HttpUtil.getString(request,"parentId", "myhome");
				if(user.getId().equals("admin")){
					menulist=menuDomain.queryTreeByParentId(parentId, 3, true, false);
				}else{
					menulist=menuDomain.querySubTreesByParentIdAndRoles(parentId, roleIds);
				}
				WebUtils.setSessionAttribute(request, "menulist", menulist);
				Menu mainMenu=(Menu)menuDomain.query("myhome");
				WebUtils.setSessionAttribute(request, "mainMenu", mainMenu);
				
				//站内通知
				DetachedCriteria znxcriteria=DetachedCriteria.forClass(WaitToRead.class);
				Page znxpage=new Page(0,5);
				znxcriteria.add(Restrictions.eq("transactId", id));
				znxcriteria.addOrder(Order.desc("id"));
				znxcriteria.add(Restrictions.eq("valid",false));//未读
				List znxlist=this.baseDomain.queryByCriteria(znxcriteria, znxpage);
				WebUtils.setSessionAttribute(request, "znxlist", znxlist);
				WebUtils.setSessionAttribute(request, "znxsize", znxlist.size());
				//我的粉丝
				DetachedCriteria fscriteria=DetachedCriteria.forClass(Fans.class);
				Page fspage=new Page(0,20);
				fscriteria.add(Restrictions.eq("fansId", id));
				fscriteria.addOrder(Order.desc("id"));
				fscriteria.add(Restrictions.eq("valid",true));//未读
				List fslist=this.baseDomain.queryByCriteria(fscriteria, fspage);
				WebUtils.setSessionAttribute(request, "fslist", fslist);
				WebUtils.setSessionAttribute(request, "fssize", fspage.getTotalCount());
				//查询自己学校的logo
				Organ organ=(Organ)this.organDomain.query(user.getOrganId());
				if(organ!=null){
					String suborganlogo=organ.getImageurl();
					String schoolimage=organ.getSchoolimage();
					if(!StringUtil.isNullOrEmpty(schoolimage)){
						WebUtils.setSessionAttribute(request, "schoolimage", schoolimage);
					}
					if(!StringUtil.isNullOrEmpty(suborganlogo)){
						WebUtils.setSessionAttribute(request, "suborganlogo", suborganlogo);
					}
					if(!StringUtil.isNullOrEmpty(organ.getOrganCode())){
						WebUtils.setSessionAttribute(request, "suborgancode", organ.getOrganCode());
					}
					WebUtils.setSessionAttribute(request, "suborganname", organ.getTname());
					//查询课程数量 微课程数量
					String wksql="select count(t.id) from t_edu_lesson as t left join t_edu_course as tc on t.courseId=tc.id where tc.organId=?";
					//查询用户数量
					String yhsql="select count(id) from t_core_user where organId=?";
					//查询课程数量
					String kcsql="select count(id) from t_edu_course where organId=?";
					int wkcount=this.jdbcTemplate.queryForInt(wksql,new Object[]{user.getOrganId()});
					int yhcount=this.jdbcTemplate.queryForInt(yhsql,new Object[]{user.getOrganId()});
					int kccount=this.jdbcTemplate.queryForInt(kcsql,new Object[]{user.getOrganId()});
					WebUtils.setSessionAttribute(request,"wkcountu", wkcount);
					WebUtils.setSessionAttribute(request,"yhcountu", yhcount);
					WebUtils.setSessionAttribute(request,"kccountu", kccount);
				}
				this.outSuccessString(request, response, "");
			}
		}
	}
	public ModelAndView toLoginQQ(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("redirect:/index.do", model);
	}
	
	/**
	 * QQ登陆
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void qqLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String url = HttpUtil.getString(request, "url", "");
		String openID = HttpUtil.getString(request, "openID", "");
		Map<String, Object> map = HttpSendUtil.get(url);
		if(map.isEmpty() && openID.equals("")){
			this.outFailString(request, response, "QQ登陆失败", "");
			return;
		}
		Users entity = (Users) usersDomain.getBaseObject();
		try {
			this.bind(request, entity);
			entity.setId(openID);
			Object obj = usersDomain.query(entity.getId());
			if (obj != null) {
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USER, obj);
				this.outSuccessString(request, response, "webIndex.do");
				return;
			} else {
				this.saveBaseInfoToObject(request, entity);
				entity.setId(openID);
				entity.setCreateId(entity.getId());
				entity.setTname((String)map.get("nickname"));
				entity.setSex((String)map.get("gender"));
				entity.setPassword(HashUtil.toMD5("123456"));
				String pingyin = Pinyin4jUtil.getPinYinHeadChar(entity
						.getTname());
				entity.setDuty("学生");
				entity.setQueryCode(pingyin);
				entity.setImageUrl((String)map.get("figureurl_qq_1"));
				Role role = (Role) this.roleDomain.query("RCOMMONUSER");
				this.usersDomain.save(entity);
				if (role != null){
					RoleUsers roleUsers = new RoleUsers();
					roleUsers.setUsersId(entity.getId());
					roleUsers.setRoleId(role.getId());
					roleUsers.setValid(true);
					roleUsers.setTname(entity.getTname());
					this.roleUsersDomain.save(roleUsers);
				}
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USER, entity);
				this.outSuccessString(request, response, "webIndex.do");
			}
		} catch (Exception e) {
			e.getStackTrace();
			this.outFailString(request, response, WEBCONSTANTS.FAILINFOR,
					"");
		}
	}
	
	/***
	 * js方式验证登录是否成功
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	public void sso(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = HttpUtil.getString(request, "id", "");
		String passwords = HttpUtil.getString(request, "password", "");
		if (id.equals("") || passwords.equals("")) {
			this.outFailString(request, response, "用户名或密码可能为空", "");
		}
		String password = HashUtil.toMD5(passwords);
		List list = this.baseDomain.queryByCriteria(DetachedCriteria
				.forClass(Users.class)
				.add(Restrictions.eq("valid", true))
				.add(Restrictions.eq("id", id))
				.add(Restrictions.or(Restrictions.eq("password", passwords),
						Restrictions.eq("password", password))));
		if (list.size() > 0) {
			this.outSuccessString(request, response, "成功");
		} else {
			this.outFailString(request, response, "用户名和密码不正确", "");
		}
	}

	/***
	 * 手机根据用户查询功能菜单页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView ssomenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		if (HttpUtil.isMobile(request)) {// 如果是移动手机终端应该返回手机页面版
			String parentId = HttpUtil
					.getString(request, "parentId", rootValue);
			List menulist = new ArrayList();
			Users user = HttpUtil.getLoginUser(request);
			if (user.getId().equals("admin")) {
				List<BaseTreeObject> nmenulist=menuDomain.queryTreeByParentId(parentId, 3, true, false);
				for (BaseTreeObject btree : nmenulist) {
					menulist.addAll(btree.getSubset());
				}
			} else {
				List<BaseTreeObject> nmenulist = menuDomain
						.querySubTreesByParentIdAndRoles(parentId,
								user.getRoles());
				for (BaseTreeObject btree : nmenulist) {
					menulist.addAll(btree.getSubset());
				}
			}
			model.put("menulist", menulist);
			return new ModelAndView(this.getCustomPage("index"), model);
		} else {
			return new ModelAndView(this.getCustomPage("index"), model);
		}
	}
	/**
	 * 查看首页
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("baseframe/index", model);
	}
	/***
	 * 登录显示顶层菜单及个人用户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView loginTop(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Users user=(Users)HttpUtil.getLoginUser(request);
		if(user==null){
			throw new NotLoginException();
		}
		Map<String, Object> model = new HashMap<String, Object>();
		List<String> roleIds= (List<String>) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS);
		Boolean flag=(Boolean) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEFLAG);
		if(flag){
			List menulist=menuDomain.queryByCriteria(this.menuDomain.getCriteria(true, false).add(Restrictions.eq("parentId",rootValue)));
			model.put("menulist", menulist);
		}else{
			List menulist=menuDomain.querySubTreeByParentIdAndRoles(rootValue,roleIds);
			model.put("menulist", menulist);
		}	
		String logosrc="theme/defaultstyle/images/logo.png";
		SystemProperty logourl=(SystemProperty)systemPropertyDomain.query("system.logo.url");
		if(logourl!=null&&null!=logourl.getTname()&&!logourl.getTname().equals("")){
			logosrc=logourl.getTname();
		}
		DetachedCriteria criteria=DetachedCriteria.forClass(FavouritesLink.class);
		criteria.add(Restrictions.eq("valid", true));
		criteria.add(Restrictions.or(Restrictions.eq("createId", user.getId()),Restrictions.eq("createId", "admin")));
		Page page=HttpUtil.convertPage(request);
		page.setPageSize(8);
		List favouriteslinks=this.baseDomain.queryByCriteria(criteria, page);
		model.put("favouriteslinks", favouriteslinks);
		model.put("logosrc", logosrc);
		SystemProperty company=(SystemProperty)systemPropertyDomain.query("system.company.name");
		SystemProperty support=(SystemProperty)systemPropertyDomain.query("system.technical.support");
		SystemProperty product=(SystemProperty)systemPropertyDomain.query("system.product.name");
		model.put("companyname", company==null?"郑州智星网络科技有限公司":company.getTname());
		model.put("supportname", support==null?"郑州智星网络科技有限公司":support.getTname());
		model.put("productname", product==null?"郑州智星网络科技有限公司":product.getTname());
		return new ModelAndView("baseframe/indexTop", model);
	}
	/***
	 * 查询当前在线用户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView currentOnlineUsers(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		Page page=HttpUtil.convertPage(request);
		ServletContext applation=this.getServletContext();
		List onlineUserlist=(List) applation.getAttribute("onlineUserlist");
		if(onlineUserlist==null){
			onlineUserlist=new ArrayList();
			applation.setAttribute("onlineUserlist", onlineUserlist);
		}
		DetachedCriteria criteria=this.baseDomain.getCriteria(true);
		List list=new ArrayList();
		if(onlineUserlist.size()>0){
			criteria.add(Restrictions.in("id", onlineUserlist));
			list=this.baseDomain.queryByCriteria(criteria,page);
		}
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView("core/users/currentOnlineUsers", model);
	}

	public ModelAndView regeditpage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		List organlist=organDomain.queryTreeByParentId(rootValue, 3, true, false);
		model.put("parentlist", organlist);
		return new ModelAndView("html/regedit", model);
	}
	/****
	 * 密码检查
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void checkpassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			String password=HttpUtil.getString(request, "password", "");
			BaseObject user=HttpUtil.getLoginUser(request);
			if(!password.equals("")&&user!=null){
				password=HashUtil.toMD5(password);
				String oldpassword="";
				if(user instanceof Users){
					oldpassword=((Users) user).getPassword();
				}
				if(password.equals(oldpassword)){
					this.outSuccessString(request,response, "密码正确，可以使用！");
				}else{
					this.outFailString(request,response, "密码不正确，检查失败！","");
				}
			}else{
				this.outFailString(request,response, "密码不正确，检查失败！","");
			}
	}
	/***
	 * 用户验证
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void nameValidation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			String id=HttpUtil.getString(request, "id");
			Object cobj=this.usersDomain.query(id);
			if(cobj==null){
				this.outSuccessString(request,response, "true");
			}else{
				this.outSuccessString(request,response, "false");
			}
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
	public void setMenuDomain(MenuDomain menuDomain) {
		this.menuDomain = menuDomain;
	}
	public void setOrganDomain(OrganDomain organDomain) {
		this.organDomain = organDomain;
	}
	public void setSystemPropertyDomain(SystemPropertyDomain systemPropertyDomain) {
		this.systemPropertyDomain = systemPropertyDomain;
	}
	public void setRoleUsersDomain(RoleUsersDomain roleUsersDomain) {
		this.roleUsersDomain = roleUsersDomain;
	}
	public void setGroupUsersDomain(GroupUsersDomain groupUsersDomain) {
		this.groupUsersDomain = groupUsersDomain;
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
	public void setMailSenderTask(MailSenderTask mailSenderTask) {
		this.mailSenderTask = mailSenderTask;
	}
	public void setSystemPropertyPersonDomain(
			SystemPropertyPersonDomain systemPropertyPersonDomain) {
		this.systemPropertyPersonDomain = systemPropertyPersonDomain;
	}
	public void setDelegationAuthorizationDomain(
			DelegationAuthorizationDomain delegationAuthorizationDomain) {
		this.delegationAuthorizationDomain = delegationAuthorizationDomain;
	}
	public void setMenuPrivilegeDomain(MenuPrivilegeDomain menuPrivilegeDomain) {
		this.menuPrivilegeDomain = menuPrivilegeDomain;
	}
	public void setRoleDomain(RoleDomain roleDomain) {
		this.roleDomain = roleDomain;
	}
}
