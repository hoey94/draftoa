package com.starsoft.cms.controller;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import com.starsoft.cms.domain.ArticleDomain;
import com.starsoft.cms.domain.ColumnsDomain;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.entity.BaseObject;
public class SearchController extends BaseAjaxController {
	private OrganDomain organDomain;
	private ColumnsDomain columnsDomain;
	private ArticleDomain articleDomain;
	private JdbcTemplate jdbcTemplate;
	/***
	 * 个人主页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject userobj=(BaseObject)WebUtils.getSessionAttribute(request,"CURRERUSER");
		model.put("haslogin", true);
		if(userobj==null){//未登录
			model.put("haslogin", false);
		}else{
			model.put("haslogin", false);
		}
		return new ModelAndView(this.getListPage(),model);
	}
	
	
	/**
	 * 列表页面
	 * @return
	 */
	public String getCustomPage(String str){
		return this.initkey+"/jiuye/"+str;
	}
	public void setOrganDomain(OrganDomain organDomain) {
		this.organDomain = organDomain;
	}
	public void setColumnsDomain(ColumnsDomain columnsDomain) {
		this.columnsDomain = columnsDomain;
	}
	public void setArticleDomain(ArticleDomain articleDomain) {
		this.articleDomain = articleDomain;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
