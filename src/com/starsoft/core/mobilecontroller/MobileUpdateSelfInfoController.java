package com.starsoft.core.mobilecontroller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.GsonUtil;
import com.starsoft.core.util.HashUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;
/****
 * 更新个人信息
 * @author Administrator
 *
 */
public class MobileUpdateSelfInfoController extends BaseMobileController implements Controller {
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
			String input = InputStream2String(request
					.getInputStream());
			if (input.trim().isEmpty()) {
				responseBean.setResultCode(HttpResponseResult.FAIL);
				responseBean.setResultDesc("未传递参数");
				responseBean.setData("");
			}else{
				Map<String,String>  map = GsonUtil.getMap(input);
				String id=map.get("id");//作业标题
				String password=map.get("password");//发布老师名称
				String newpassword=map.get("newpassword");//发布老师标识
				String sex=map.get("sex");//性别
				String mobilNumber=map.get("mobilNumber");//手机号码
				String email=map.get("email");//邮箱地址
				String qqNumber=map.get("qqNumber");//qq号码
				String imageUrl=map.get("imageUrl");//个人头像
				String remarker=map.get("remarker");//个人签名
				if(StringUtil.isNullOrEmpty(id)){
					responseBean.setResultCode(HttpResponseResult.FAIL);
					responseBean.setResultDesc("用户标识不能为空");
					responseBean.setData("");
				}else{
					Users users=(Users)usersDomain.query(id);
					if(users!=null){//是老师
						if(!StringUtil.isNullOrEmpty(sex)){
							users.setSex(sex);
						}
						if(!StringUtil.isNullOrEmpty(mobilNumber)){
							users.setMobilNumber(mobilNumber);
						}
						if(!StringUtil.isNullOrEmpty(email)){
							users.setEmail(email);
						}
						if(!StringUtil.isNullOrEmpty(qqNumber)){
							users.setQqNumber(qqNumber);
						}
						if(!StringUtil.isNullOrEmpty(imageUrl)){
							users.setImageUrl(imageUrl);
						}
						if(!StringUtil.isNullOrEmpty(remarker)){
							users.setRemarker(remarker);
						}
						if(!StringUtil.isNullOrEmpty(password)&&!StringUtil.isNullOrEmpty(newpassword)&&!password.equals(newpassword)){//要更新密码
							if(HashUtil.toMD5(password).equals(users.getPassword())){//原密码相同
								users.setPassword(HashUtil.toMD5(newpassword));
								responseBean.setResultCode(HttpResponseResult.SUCCESS);
								responseBean.setResultDesc("更新成功");
								responseBean.setData("");
							}else{
								responseBean.setResultCode(HttpResponseResult.FAIL);
								responseBean.setResultDesc("旧密码提供不正确，更新失败");
								responseBean.setData("");
							}
						}else{//不更新密码
							responseBean.setResultCode(HttpResponseResult.SUCCESS);
							responseBean.setResultDesc("更新成功");
							responseBean.setData("");
						}
						
					}else{
						responseBean.setResultCode(HttpResponseResult.FAIL);
						responseBean.setResultDesc("用户不存在");
						responseBean.setData("");
					}
				}
			}
		} catch (Exception e) {
			logger.error("HttpDealError", e);
			responseBean.setResultCode(HttpResponseResult.FAIL);
			responseBean.setResultDesc(WEBCONSTANTS.FAILINFOR);
		}
		outputResponseMsg(response, GsonUtil.getJson(responseBean));
		return null;
	}
}
