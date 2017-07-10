package com.starsoft.core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.RoleUsers;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HashUtil;
import com.starsoft.core.util.HttpUtil;
/***
 * 用户注册服务
 * @author Administrator
 *
 */
public class RegeditController extends BaseAjaxController {
	private UsersDomain usersDomain;
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		String id=HttpUtil.getString(request, "id","");
		String passwords=HttpUtil.getString(request, "password","");
		if(id.equals("")||passwords.equals("")){
			String msg="帐号或者密码为空!";
			return new ModelAndView("register", model);
		}
		Users user=(Users)usersDomain.query(id);
		if(user==null){//帐号不存在
			 user=new Users();
			 this.bind(request, user);
			 String password=HashUtil.toMD5(passwords);
			 user.setPassword(password);
			 user.setOrganId("10000");
			 user.setCreateId("系统注册");
			 user.setValid(true);
			 user.setSex("学生");
			 usersDomain.save(user);
			 //增加默认普通人员角色
			RoleUsers roleUsers = new RoleUsers();
			roleUsers.setUsersId(user.getId());
			roleUsers.setRoleId("RCOMMONUSER");
			roleUsers.setValid(true);
			roleUsers.setTname(user.getTname());
			this.usersDomain.save(roleUsers);
			 
			 model.put("msg", "注册成功");
			 model.put("id", id);
			 model.put("password", passwords);
			 return new ModelAndView("login", model);
		}else{//注册失败帐号已经存在
			String msg="帐号已经存在!";
			model.put("msg", msg);
			return new ModelAndView("register",model);
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
			if(user==null){//帐号不存在
				 user=new Users();
				 this.bind(request, user);
				 String password=HashUtil.toMD5(passwords);
				 user.setPassword(password);
				 user.setOrganId("10000");
				 user.setCreateId("系统注册");
				 user.setValid(true);
				 user.setTname(user.getMobilNumber());
				 user.setSex("未知");
				 user.setDuty("学生");
				 usersDomain.save(user);
				 //增加默认普通人员角色
				RoleUsers roleUsers = new RoleUsers();
				roleUsers.setUsersId(user.getId());
				roleUsers.setRoleId("RCOMMONUSER");
				roleUsers.setValid(true);
				roleUsers.setTname(user.getTname());
				this.usersDomain.save(roleUsers);
				this.outSuccessString(request, response, "");
			}else{//注册失败帐号已经存在
				String msg="帐号已经存在!";
				this.outFailString(request, response, msg, "");
			}
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
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
}
