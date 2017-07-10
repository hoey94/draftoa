package com.starsoft.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.cms.domain.ColumnsDomain;
import com.starsoft.cms.entity.Columns;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.controller.BaseTreeController;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.Pinyin4jUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.WEBCONSTANTS;

public class ColumnsController extends BaseTreeController implements BaseInterface {
	private ColumnsDomain columnsDomain;
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView subframetree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		Users user=HttpUtil.getLoginUser(request);
		List list= columnsDomain.queryTreeByParentId(this.rootValue,4,null,false);
		model.put("list", list);
		model.put("title", "栏目");
		model.put("deleteAndAdd", "true");// 添加节点和删除节点的权利
		model.put("rightContent", "?action=list&sortfield=sortCode&sortvalue=false");
		model.put("urlLink", "?action=read&id=");
		model.put("addLink", "columns.do?action=add&parentId=");
		return new ModelAndView("baseframe/subframetree",model);
	}
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
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		criteria.add(Restrictions.isNotNull("parentId"));
		List list= columnsDomain.queryTreeByParentId(this.rootValue,4,null,false);
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getListPage(),model);
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView model=this.edit(request, response);
		return new ModelAndView(this.getReadPage(),model.getModel());
	}
	/***
	 * 公共的新建方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String parentId=HttpUtil.getString(request, "parentId", "");
		List parentlist= columnsDomain.queryTreeByParentId(rootValue, 4, true, false);
		Columns columns=new Columns();
		model.put("parentlist", parentlist);
		model.put("parentId", parentId);
		return new ModelAndView(this.getAddPage(),model);
	}
	/***
	 * 批量创建新栏目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addColumns(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String parentId=HttpUtil.getString(request, "parentId", "");
		List parentlist= columnsDomain.queryTreeByParentId(rootValue, 4, true, false);
		model.put("parentlist", parentlist);
		model.put("parentId", parentId);
		return new ModelAndView(this.getCustomPage("addColumns"),model);
	}
	
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject obj=(BaseObject) baseDomain.query(id);
		if(obj!=null){
			List parentlist=columnsDomain.queryTreeByBaseObjectIdNotContainId(rootValue, id, 4, null, false);
			model.put("parentlist", parentlist);
			model.put("obj", obj);
			model.put("page", page);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		return new ModelAndView(this.getEditPage(),model);
	}
	/***
	 * 保存
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Columns entity=(Columns)baseDomain.getBaseObject();
		String gotourl=request.getRequestURI();
		try{
			this.bind(request, entity);
			this.saveBaseInfoToObject(request, entity);
			if(entity.getParentId()==null||"".equals(entity.getParentId())){
				entity.setParentId(rootValue);
				entity.setSortCode(columnsDomain.getMaxSortCode());
			}else{
				entity.setSortCode(columnsDomain.getMaxSortCodeByProperty("parentId", entity.getParentId()));
			}
			this.baseDomain.save(entity);
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/***
	 * 保存
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void saveColumns(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gotourl=request.getRequestURI();
		List<String> columnNames=HttpUtil.getList(request, "columnName", "#");
		try{
			List saveList=new ArrayList();
			for(String tname:columnNames){
				Columns entity=(Columns)baseDomain.getBaseObject();
				this.bind(request, entity);
				entity.setTname(tname);
				String id=Pinyin4jUtil.getPinYinHeadChar(tname.trim());
				Object obj=this.baseDomain.query(id);
				if(obj!=null){
					for(int t=1;t<5;t++){
						obj=this.baseDomain.query(id+t);
						if(obj==null){
							id=id+t;
							break;
						} 
					}
				}
				entity.setId(id);
				this.saveBaseInfoToObject(request, entity);
				if(entity.getParentId()==null||"".equals(entity.getParentId())){
					entity.setParentId(rootValue);
					entity.setSortCode(columnsDomain.getMaxSortCode());
				}else{
					entity.setSortCode(columnsDomain.getMaxSortCodeByProperty("parentId", entity.getParentId()));
				}
				this.baseDomain.save(entity);
			}
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	
	/****
	 * 获取栏目名称的英文全称代码标识
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getTnameId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tname=HttpUtil.getString(request, "tname", "");
		if(!tname.equals("")){
			String pingyin=Pinyin4jUtil.getPinYinHeadChar(tname.trim());
			Object obj=this.baseDomain.query(pingyin);
			if(obj!=null){
				for(int t=1;t<20;t++){
					obj=this.baseDomain.query(pingyin+t);
					if(obj==null){
						pingyin=pingyin+t;
						break;
					} 
				}
			}
			HttpResponseResult resultBean=new HttpResponseResult(HttpResponseResult.SUCCESS, pingyin,"");
			request.setAttribute("lastAjaxActionResult", HttpResponseResult.SUCCESS);
			this.outJsonObject(response, resultBean);
		}else{
			this.outFailString(request, response, "", "");
		}
	}
	/***
	 * 更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void update(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		String gotourl=request.getRequestURI()+"?action=edit&id="+id;
		try{
			Columns entity=(Columns) baseDomain.query(id);
			if(entity!=null){
				this.bind(request, entity);
			}
			if(entity.getParentId()==null||"".equals(entity.getParentId())){
				entity.setParentId(rootValue);
			}
			this.baseDomain.update(entity);
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	public void setColumnsDomain(ColumnsDomain columnsDomain) {
		this.columnsDomain = columnsDomain;
		this.setBaseTreeDomain(columnsDomain);
	}	
	
}
