package com.starsoft.core.mobilecontroller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.GsonUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.UserInfo;
/***
 * 获取个人通讯录
 * @author Administrator
 *
 */
public class MobileContactsController extends BaseMobileController implements Controller {
	private UsersDomain usersDomain;
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpResponseResult responseBean=new HttpResponseResult();
		try {
			String input = InputStream2String(request.getInputStream());
			if (input.trim().isEmpty()) {
				List result=usersDomain.queryAll();
				JSONArray jsonArray = new JSONArray();
				for(int i=0;i<result.size();i++){
					jsonArray.add(JSONObject.fromObject(result.get(i)));
				}
				responseBean.setResultCode(HttpResponseResult.SUCCESS);
				responseBean.setResultDesc("通讯录查询成功");
				responseBean.setData(jsonArray.toString());
			}else{
				Map<String,String>  map = GsonUtil.getMap(input);
				String organId=map.get("organId");//部门名称
				String duty=map.get("duty");
				DetachedCriteria criteria=usersDomain.getCriteria(true);
				if(!StringUtil.isNullOrEmpty(organId)){
					criteria.add(Restrictions.eq("organId", organId));
				}
				if(!StringUtil.isNullOrEmpty(duty)){
					criteria.add(Restrictions.eq("duty", duty));
				}
				List<Users> usersList=usersDomain.queryByCriteria(criteria);
				JSONArray jsonArray = new JSONArray();
				for(Users users:usersList){
					jsonArray.add(JSONObject.fromObject(new UserInfo(users.getId(),users.getTname(),"",users.getMobilNumber(),users.getOrganId(),users.getOrganId(),users.getSex(),users.getDuty())));
				}
				if (jsonArray.size() < 1) {
					responseBean.setResultCode(HttpResponseResult.SUCCESS);
					responseBean.setResultDesc("没有人员信息");
					responseBean.setData(null);
				}else{
					responseBean.setResultCode(HttpResponseResult.SUCCESS);
					responseBean.setResultDesc("通讯录查询成功");
					responseBean.setData(jsonArray.toString());
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
