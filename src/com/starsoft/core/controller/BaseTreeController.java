package com.starsoft.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.domain.BaseTreeDomain;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.WEBCONSTANTS;

public abstract class BaseTreeController extends BaseAjaxController {
	public static final String CommonTreeSelectPage="common/commonTreeSelectPage";
	private BaseTreeDomain baseTreeDomain;
	/***
	 * 查询规则抽取
	 * @param request
	 * @return
	 */
	@Override
	protected DetachedCriteria  convertCriteria(HttpServletRequest request){
		DetachedCriteria criteria=null;
		String valid=HttpUtil.getString(request, "valid", "null");
		if("true".equals(valid)){
			criteria = baseDomain.getCriteria(true);
		}else if("false".equals(valid)){
			criteria = baseDomain.getCriteria(false);
		}else{
			criteria = baseDomain.getCriteria(null);
		}
		String name=HttpUtil.getString(request, "tname","");
		if(!name.equals("")){
			criteria.add(Restrictions.ilike("tname", "%"+name+"%"));
		}
		String sortfield =HttpUtil.getString(request, "sortfield","id");
		boolean sortValue=HttpUtil.getBoolean(request, "sortvalue", true);
		if(sortValue){
			criteria.addOrder(Order.desc(sortfield));
		}else{
			criteria.addOrder(Order.asc(sortfield));
		}
		return criteria;
	}
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView frame(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(HttpUtil.isMobile(request)){
			String requestCon=request.getServletPath();
			request.getRequestDispatcher(requestCon+"?action=subframetree").forward(request, response);
			return null;
		}else{
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("rightContent", "?action=list");
			model.put("leftTree", "?action=subframetree");
			return new ModelAndView("baseframe/subframes",model);
		}
	}
	/**
	 * 获取子节点对象
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getSubNode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String parentId=HttpUtil.getString(request, "parentId", ";");
		List list=baseTreeDomain.queryByParentId(parentId, true, false);
		if(list.size()>0){
			JSONArray jsonArray = new JSONArray();
			for(int t=0;t<list.size();t++){
				jsonArray.add(JSONObject.fromObject(list.get(t)));
			}
			HttpResponseResult resultBean=new HttpResponseResult(HttpResponseResult.SUCCESS, WEBCONSTANTS.SUCCESSINFOR,jsonArray.toString());
			this.outJsonObject(response,resultBean);
		}
		
	}
	/***
	 * 公共的树形结构选择列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public ModelAndView commonselectlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String objectId=HttpUtil.getString(request, "objectId","");
		String objectName=HttpUtil.getString(request, "objectName","");
		String selectType=HttpUtil.getString(request, "selectType","0");
		String parentId=HttpUtil.getString(request, "parentId",rootValue);
		List list= baseTreeDomain.queryTreeByParentId(parentId,1,null,false);
		if("0".equals(selectType)){//多选
			model.put("selectType", true);
		}else{
			model.put("selectType", false);
		}
		model.put("list", list);		
		model.put("objectId", objectId);		
		model.put("objectName", objectName);		
		return new ModelAndView(CommonTreeSelectPage,model);
	}
	public void setBaseTreeDomain(BaseTreeDomain baseTreeDomain) {
		this.baseTreeDomain = baseTreeDomain;
	}
}
