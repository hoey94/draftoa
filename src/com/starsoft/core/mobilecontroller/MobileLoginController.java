package com.starsoft.core.mobilecontroller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.LogInRecord;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.GsonUtil;
import com.starsoft.core.util.HashUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.UserInfo;

/****
 * 手机统一登录口
 * @author Administrator
 *
 */
public class MobileLoginController extends BaseMobileController implements Controller {
	private UsersDomain usersDomain;
	/***
	 * 手机用户登录验证
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpResponseResult responseBean=new HttpResponseResult();
		try {
			// 输入数据
			String input = InputStream2String(request.getInputStream());
			if (input.trim().isEmpty()) {
				responseBean.setResultCode(HttpResponseResult.FAIL);
				responseBean.setResultDesc("传递参数不能为空！");
			}else{
				Map<String,String>  map = GsonUtil.getMap(input);
				String id=map.get("id");
				String password =map.get("password");
				if(id.equals("")||password.equals("")){
					responseBean.setResultCode(HttpResponseResult.FAIL);
					responseBean.setResultDesc("用户名或密码不能为空");
				}else{
					Users users=(Users)usersDomain.query(id);
					if(users==null){
						if(StringUtil.isMobileNumber(id)){//手机号码输入
							users=(Users)usersDomain.queryFirstByProperty("mobilNumber", id);
						}
					}
					if(users!=null){
						if(HashUtil.toMD5(password).equals(users.getPassword())){
							UserInfo data=new UserInfo(users.getId(),users.getTname(),password,users.getMobilNumber(),users.getOrganId(),users.getOrganId(),users.getSex(),users.getDuty());
							responseBean.setData(GsonUtil.getJson(data));
							responseBean.setResultCode(HttpResponseResult.SUCCESS);
							responseBean.setResultDesc("登录成功！");
							LogInRecord logInRecord=new LogInRecord();
							logInRecord.setCreateId(id);
							logInRecord.setTname("登录成功");
							logInRecord.setValid(true);
							logInRecord.setBrowserType("android");
							logInRecord.setIp(request.getRemoteAddr());
							this.usersDomain.save(logInRecord);
						}else{
							responseBean.setResultCode(HttpResponseResult.FAIL);
							responseBean.setResultDesc("用户密码不正确！");
							//登录失败记录
							LogInRecord logInRecord=new LogInRecord();
							logInRecord.setCreateId(id);
							logInRecord.setTname("登录失败");
							logInRecord.setValid(false);
							logInRecord.setIp(request.getRemoteAddr());
							logInRecord.setBrowserType("android");
							this.usersDomain.save(logInRecord);
						}
					}else{
						responseBean.setResultCode(HttpResponseResult.FAIL);
						responseBean.setResultDesc("用户名不正确，用户不存在！");
					}
				}
			}
		} catch (Exception e) {
			logger.error("HttpDealError", e);
			responseBean.setResultCode(HttpResponseResult.FAIL);
			responseBean.setResultDesc(WEBCONSTANTS.FAILINFOR);
		}
		outputResponseMsg(response,GsonUtil.getJson(responseBean));
		return null;
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
}
