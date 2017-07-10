package com.starsoft.core.mobilecontroller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.starsoft.core.util.GsonUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.domain.ClientVersionDomain;
import com.starsoft.core.entity.ClientVersion;
/****
 * 学校通知列表
 * @author Administrator
 *
 */
public class MobileVersionController extends BaseMobileController implements Controller {
	private ClientVersionDomain clientVersionDomain;
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
			logger.info("INPUT:" + input);
			if (input.trim().isEmpty()) {
				responseBean.setResultDesc("未传递参数");
				responseBean.setData("");
			}else{
				Map<String,String>  map = GsonUtil.getMap(input);
				String id =map.get("id");
				String clientType =map.get("clientType");// android or ios
				if(StringUtil.isNullOrEmpty(id)||StringUtil.isNullOrEmpty(clientType)){
					responseBean.setResultCode("0");
					responseBean.setResultDesc("传递参数不能为空值");
					responseBean.setData("");
				}else{
					ClientVersion clientVersion=clientVersionDomain.queryNewVersionByClientCodeAndClientType(id, clientType);
					if(clientVersion!=null){
						responseBean.setData(JSONObject.fromObject(clientVersion).toString());
						responseBean.setResultCode("1");
						responseBean.setResultDesc("查询成功");
					}else{
						responseBean.setData(JSONObject.fromObject(clientVersion).toString());
						responseBean.setResultCode("0");
						responseBean.setResultDesc("查询成失败,不存在该客户端数据");
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
	public void setClientVersionDomain(ClientVersionDomain clientVersionDomain) {
		this.clientVersionDomain = clientVersionDomain;
	}

}
