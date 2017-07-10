package com.starsoft.core.controller;

import java.util.ArrayList;
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
import org.springframework.web.util.WebUtils;

import com.starsoft.core.domain.MenuDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.HelpGuide;
import com.starsoft.core.entity.Menu;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class HelpGuideController extends BaseTreeController implements BaseInterface {
	private MenuDomain menuDomain;
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
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		List<String> roleIds= (List<String>) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS);
		BaseObject user=HttpUtil.getLoginUser(request);
		List list=new ArrayList();
		if(user.getId().equals("admin")){
			list=menuDomain.queryTreeByParentId(parentId, 6, true, false);
		}else{
			list=menuDomain.querySubTreesByParentIdAndRoles(parentId, roleIds);
		}
		String id=rootValue;
		if(list.size()>0){
			Menu menu=(Menu)list.get(0);
			id=menu.getId();
		}
		model.put("list", list);
		model.put("title", "菜单");
		model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
		model.put("rightContent", "?action=edit&id="+id);
		model.put("urlLink", "?action=edit&id=");
		model.put("addLink", "menu.do?action=add&parentId=");
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
			criteria.addOrder(Order.asc(sortfield));
		}else{
			criteria.addOrder(Order.desc(sortfield));
		}
		List list= baseDomain.queryByCriteria(criteria, page);
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
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		List parentlist= menuDomain.queryTreeByParentId(parentId,5,null,false);
		model.put("parentlist", parentlist);
		return new ModelAndView(this.getAddPage(),model);
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
		if(!id.equals("")){
			BaseObject obj=(BaseObject) baseDomain.query(id);
			if(obj!=null){
				model.put("obj", obj);
				model.put("page", page);
			}else{
				obj=baseDomain.getBaseObject();
				obj.setId(id);
				Menu menu=(Menu)menuDomain.query(id);
				if(menu!=null){
					obj.setTname(menu.getTname());
				}
				model.put("obj", obj);
				model.put("page", page);
			}
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
		String gotourl="";
		String method=request.getMethod().toLowerCase();
		gotourl=request.getRequestURI();
		if("post".equals(method)){
			try{
				HelpGuide entity=(HelpGuide)baseDomain.getBaseObject();
				this.bind(request, entity);
				this.saveBaseInfoToObject(request, entity);
				this.baseDomain.save(entity);
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
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
		String gotourl=HttpUtil.getString(request, "gotourl", "?action=list");
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				BaseObject entity=(BaseObject) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
					this.baseDomain.update(entity);
				}else{
					entity=(BaseObject)baseDomain.getBaseObject();
					entity.setId(id);
					this.bind(request, entity);
					this.saveBaseInfoToObject(request, entity);
					this.baseDomain.save(entity);
				}
				gotourl=request.getRequestURI()+"?action=edit&id="+id;
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/**
	 * 标签删除对象功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void getSubNode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String parentId=HttpUtil.getString(request, "parentId", ";");
		List list=menuDomain.queryByParentId(parentId, true, false);
		if(list.size()>0){
			JSONArray jsonArray = new JSONArray();
			for(int t=0;t<list.size();t++){
				jsonArray.add(JSONObject.fromObject(list.get(t)));
			}
			this.outSuccessString(request,response, jsonArray.toString());
		}
	}
	/***
	 * 查看帮助
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewcurrenthelp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		List list= menuDomain.queryTreeByParentId(parentId,5,null,false);
		String id=rootValue;
		if(list.size()>0){
			Menu menu=(Menu)list.get(0);
			id=menu.getId();
		}
		model.put("list", list);
		model.put("title", "菜单");
		model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
		model.put("rightContent", "?action=edit&id="+id);
		model.put("urlLink", "?action=edit&id=");
		model.put("addLink", "menu.do?action=add&parentId=");
		return new ModelAndView(this.getCustomPage("Index"),model);
	}
	/***
	 * 查看具体的帮助信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewhelp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String id=HttpUtil.getString(request, "id", rootValue);
		HelpGuide helpGuide=(HelpGuide)this.baseDomain.query(id);
		if(helpGuide!=null){
			model.put("helpGuide", helpGuide);
		}else{
			helpGuide=(HelpGuide)this.baseDomain.getBaseObject();
			helpGuide.setTname("暂没发现");
			model.put("helpGuide", helpGuide);
		}
		List subMenu=this.menuDomain.queryByParentId(id, true, false);
		model.put("subMenu", subMenu);
		return new ModelAndView(this.getCustomPage("Detail"),model);
	}
	/***
	 * 帮助树列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView menutree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String parentId=HttpUtil.getString(request,"parentId", rootValue);
		List<String> roleIds= (List<String>) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS);
		BaseObject user=HttpUtil.getLoginUser(request);
		List list=new ArrayList();
		if(user.getId().equals("admin")){
			list=menuDomain.queryTreeByParentId(parentId, 3, true, false);
		}else{
			list=menuDomain.querySubTreesByParentIdAndRoles(parentId, roleIds);
		}
		model.put("list", list);
		if(list.size()>0){
			Menu menu=(Menu)list.get(0);
			model.put("treeroot", menu.getParentId());
		}
		return new ModelAndView(this.getCustomPage("Menuleft"), model);
	}
	public void setMenuDomain(MenuDomain menuDomain) {
		this.menuDomain = menuDomain;
		this.setBaseTreeDomain(menuDomain);
	}
	

}
