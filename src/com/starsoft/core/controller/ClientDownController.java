package com.starsoft.core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.entity.ClientVersion;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;

public class ClientDownController extends BaseAjaxController {
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		ClientVersion weikeapp= (ClientVersion)baseDomain.queryFirstByProperty("clientCode", "weike");
		ClientVersion weiketvapp= (ClientVersion)baseDomain.queryFirstByProperty("clientCode", "weiketv");
		ClientVersion weikepadapp= (ClientVersion)baseDomain.queryFirstByProperty("clientCode", "weikepad");
		if(weikeapp!=null){
			model.put("weikeapp", weikeapp);
		}
		if(weiketvapp!=null){
			model.put("weiketvapp", weiketvapp);
		}
		if(weikepadapp!=null){
			model.put("weikepadapp", weikepadapp);
		}
		return new ModelAndView(this.getListPage(),model);
	}
}
