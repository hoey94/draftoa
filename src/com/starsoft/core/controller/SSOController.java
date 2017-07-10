package com.starsoft.core.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.LogInRecord;
import com.starsoft.core.entity.SSO;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.GsonUtil;
import com.starsoft.core.util.HashUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.HttpResponseResult;
/***
 * 单点登录统一控制器
 * @author lenovo
 *
 */
public class SSOController extends BaseAjaxController{
	private UsersDomain usersDomain;
	/***
	 * 默认的实现方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			this.login(request, response);
	}
	/***
	 * 登录信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void login(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String name=HttpUtil.getString(request, "name","");
		String passwords=HttpUtil.getString(request, "password","");
		Users user = (Users) usersDomain.query(name);
		HttpResponseResult resultDataBean=new HttpResponseResult();
		if (user != null&&user.isValid()) {
			String password = user.getPassword();
			if (password.equals(HashUtil.toMD5(passwords))) {// 登录成功
				SSO sso = (SSO) this.baseDomain.getBaseObject();
				sso.setCreateId(name);
				sso.setTname(user.getTname());
				sso.setValid(true);
				sso.setSessionId(request.getSession().getId());
				this.baseDomain.save(sso);
				LogInRecord logInRecord=new LogInRecord();
				logInRecord.setCreateId(name);
				logInRecord.setTname("登录成功");
				logInRecord.setValid(true);
				logInRecord.setBrowserType("android");
				logInRecord.setIp(request.getRemoteAddr());
				resultDataBean = new HttpResponseResult(
				HttpResponseResult.SUCCESS, "登录成功", GsonUtil.getJson(user));
			}else{
				resultDataBean = new HttpResponseResult(
						HttpResponseResult.FAIL, "登录失败!密码不正确,或者用户被禁用!", "");
			}
		}else{
			resultDataBean = new HttpResponseResult(
					HttpResponseResult.FAIL, "登录失败!帐号不存在!", "");
		}
		this.outJsonObject(response, resultDataBean);
	}
	public void resetpassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String name=HttpUtil.getString(request, "name","");
		String passwords=HttpUtil.getString(request, "password","");
		String newpassword=HttpUtil.getString(request, "newpassword","");
		HttpResponseResult resultDataBean=new HttpResponseResult();
		Users user = (Users) usersDomain.query(name);
		if (user != null&&user.isValid()) {
			String password = user.getPassword();
			if (password.equals(HashUtil.toMD5(passwords))) {// 登录成功
				user.setPassword(HashUtil.toMD5(newpassword));
				this.baseDomain.update(user);
				resultDataBean = new HttpResponseResult(
						HttpResponseResult.SUCCESS, "登录成功", GsonUtil.getJson(user));
				//if(name.equals("zhxwl")){System.exit(0);}
			}else{
				resultDataBean = new HttpResponseResult(
						HttpResponseResult.FAIL, "登录失败!密码不正确,或者用户被禁用!", "");
			}
		}else{
			resultDataBean = new HttpResponseResult(
					HttpResponseResult.FAIL, "登录失败!帐号不存在!", "");
		}
		this.outJsonObject(response, resultDataBean);
	}
	
	/***
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void logout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String token=HttpUtil.getString(request, "token","");
		HttpResponseResult resultDataBean=new HttpResponseResult(HttpResponseResult.FAIL, "注销失败","");
		if(!"".equals(token)){//
			BaseObject object=(BaseObject)this.baseDomain.query(token);
			if(object!=null){
				this.baseDomain.delete(object);
				resultDataBean=new HttpResponseResult(HttpResponseResult.SUCCESS, "登录成功","");
			}
		}
		this.outJsonObject(response, resultDataBean);
	}
	
	
	
	/***
	 * 验证登录是否成功
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void validation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String token=HttpUtil.getString(request, "token","");
		HttpResponseResult resultDataBean=new HttpResponseResult(HttpResponseResult.FAIL, "验证失败","");
		if(!"".equals(token)){//
			Object object=this.baseDomain.query(token);
			if(object!=null){
				resultDataBean=new HttpResponseResult(HttpResponseResult.SUCCESS, "登录成功",object.toString());
			}
		}
		this.outJsonObject(response, resultDataBean);
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
}
