package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.starsoft.core.domain.AppesActionPrivilegeDomain;
import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.domain.MenuDomain;
import com.starsoft.core.domain.MenuPrivilegeDomain;
import com.starsoft.core.domain.RoleDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Menu;
import com.starsoft.core.entity.MenuPrivilege;
import com.starsoft.core.entity.Role;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class MenuPrivilegeController extends BaseAjaxController implements BaseInterface {
	private MenuDomain menuDomain;
	private RoleDomain roleDomain;
	private AppesDomain appesDomain;
	private AppesActionPrivilegeDomain appesActionPrivilegeDomain;
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView frame(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		Users user=HttpUtil.getLoginUser(request);
		List roles=user.getRoles();
//		List list= menuDomain.queryTreeByParentId(parentId,5,null,false);
		List list= menuDomain.querySubTreesByParentIdAndRoles(parentId, roles);
		model.put("list", list);
		model.put("title", "菜单");
		model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
		model.put("rightContent", "?action=list");
		model.put("urlLink", "?action=list&menuId=");
		model.put("subNodeUrl", "menu.do?action=getSubNode&parentId=");
		model.put("addLink", "menu.do?action=add&parentId=");
		return new ModelAndView("baseframe/subframe",model);
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
		String menuId=HttpUtil.getString(request, "menuId", "");
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = baseDomain.getCriteria(true);
		Menu menu=null;
		List list=new ArrayList();
		List selectlist=new ArrayList();
		if(!menuId.equals("")){
			menu=(Menu) menuDomain.query(menuId);
			DetachedCriteria criteriatemp=DetachedCriteria.forClass(Role.class).add(Restrictions.eq("valid", true)).addOrder(Order.asc("id"));
			Users user=HttpUtil.getLoginUser(request);
			List roles=user.getRoles();
			criteriatemp.add(Restrictions.in("id", roles));
			list=this.baseDomain.queryByCriteria(criteriatemp, page);
			if(menu!=null){
				criteria.add(Restrictions.eq("menuId", menuId));
				criteria.add(Restrictions.eq("valid", true));
				selectlist= baseDomain.queryByCriteria(criteria);
			}else{
				menu=new Menu();
				menu.setTname("所有");
			}
		}else{
			menu=new Menu();
			menu.setTname("所有");
		}
		model.put("menu", menu);
		model.put("list", list);
		model.put("selectlist", selectlist);
		model.put("page", page);
		if("所有".equals(menu.getTname())){
			return new ModelAndView(this.getCustomPage("All"),model);
		}
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
	 * 分角色查询应用权限
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listbyrole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		String roleId=HttpUtil.getString(request, "roleId", "");
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		Page page=HttpUtil.convertPage(request);
		Role role=(Role) roleDomain.query(roleId);
		List<String> roleIds= (List<String>) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS);
		BaseObject user=HttpUtil.getLoginUser(request);
		List menulist=new ArrayList();
		if(user.getId().equals("admin")){
			menulist=menuDomain.queryTreeByParentId(parentId, 3, true, false);
		}else{
			menulist=menuDomain.querySubTreesByParentIdAndRoles(parentId, roleIds);
		}
		List menulists=menuDomain.queryTreeByParentId(parentId,5,null,false);
		if(role!=null&&menulist.size()>0){
			DetachedCriteria criteria= baseDomain.getCriteria(null);
			criteria.add(Restrictions.eq("roleId", roleId));
//			criteria.add(Restrictions.in("menu", menulist));
			criteria.add(Restrictions.eq("valid", true));
			List selectlist= baseDomain.queryByCriteria(criteria);
			model.put("selectlist", selectlist);
		}else{
			role.setTname("所有");
		}
		model.put("role", role);
		model.put("page", page);
		model.put("pmenulist", menulist);
		
		
		return new ModelAndView(this.getCustomPage("listbyrole"),model);
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
		String menuId=HttpUtil.getString(request, "menuId", "");
		List rolelistall=roleDomain.queryAll();
		List rolelist=new ArrayList();
		List selectList=baseDomain.queryByProperty("menu.id", menuId);
		StringBuffer selectroleids=new StringBuffer();
		for(int t=0;t<selectList.size();t++){
			MenuPrivilege privilege=(MenuPrivilege)selectList.get(t);
			selectroleids.append(privilege.getRoleId());
		}
		for(int t=0;t<rolelistall.size();t++){
			Role role=(Role)rolelistall.get(t);
			if(selectroleids.indexOf(role.getId())==-1){
				rolelist.add(role);
			}
		}
		model.put("rolelist", rolelist);
		model.put("selectList", selectList);
		model.put("menuId", menuId);
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
		BaseObject obj=(BaseObject) baseDomain.query(id);
		if(obj!=null){
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
		String menuId=HttpUtil.getString(request, "menuId", "");
		List resultlist=HttpUtil.getList(request, "resultvalue", ",");
		String gotourl="";
		try{
			((MenuPrivilegeDomain)baseDomain).updatePrivilege(menuId, resultlist);
			gotourl=request.getRequestURI()+"?menuId="+menuId;
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
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
		gotourl=request.getRequestURI()+"?page="+page;
		try{
			BaseObject entity=(BaseObject) baseDomain.query(id);
			if(entity!=null){
				this.bind(request, entity);
			}
			this.baseDomain.update(entity);
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/***
	 * ajax更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void jsupdate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String roleId=HttpUtil.getString(request, "roleId", "");
		String menuId=HttpUtil.getString(request, "menuId", "");
		Boolean result=HttpUtil.getBoolean(request, "result", true);
		try{
			if(!roleId.equals("")&&!menuId.equals("")){
				Menu menu=(Menu) menuDomain.query(menuId);
				String url=menu.getUrl();
				String defaultAction="list";
				if(url.indexOf(".do")>-1){//有针对的控制器应用
					String appesId=url.substring(0, url.indexOf(".do"));
					if(url.indexOf("action")>-1){
						defaultAction=url.substring(url.indexOf("action")+6, url.length());
						if(defaultAction.indexOf("&")>0){
							defaultAction=defaultAction.substring(0,defaultAction.indexOf("&")).trim();
						}else{
							defaultAction=defaultAction.trim();
						}
					}
					Appes appes=appesDomain.getAppes(appesId);
					if(appes!=null&&appes.getActionList().size()>0){
						if(result){
							appesActionPrivilegeDomain.addAllPrivilegeForRole(appes, roleId, defaultAction);
						}else{
							appesActionPrivilegeDomain.deletePrivilegeForRole(appes, roleId, defaultAction);
						}
					}
				}
				if(result){
					this.updateparent(request, menuId, roleId);
				}else{
					DetachedCriteria criteria=baseDomain.getCriteria(null);
					criteria.add(Restrictions.eq("roleId", roleId));
					criteria.add(Restrictions.eq("menuId", menuId));
					List list=baseDomain.queryByCriteria(criteria);
					if(list.size()==0){
						MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
						entity.setMenuId(menuId);
						entity.setRoleId(roleId);
						entity.setValid(result);
						entity.setTname(menu.getTname());
						this.saveBaseInfoToObject(request, entity);
						entity.setValid(result);
						this.baseDomain.save(entity);
					}else if(list.size()==1){
						MenuPrivilege entity=(MenuPrivilege)list.get(0);
						entity.setValid(result);
						this.baseDomain.update(entity);
					}else{
						MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
						entity.setMenuId(menuId);
						entity.setRoleId(roleId);
						entity.setValid(result);
						entity.setTname(menu.getTname());
						this.saveBaseInfoToObject(request, entity);
						entity.setValid(result);
						List saveList=new ArrayList();
						saveList.add(entity);
						this.baseDomain.deleteAndSaveAndUpdate(list, saveList,null);
					}
				}
			}else{
				this.outFailString(request,response, "更新失败！","");
			}
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, "更新失败！","");
		}
		
	}
	private void updateparent(HttpServletRequest request,String menuId, String roleId) throws Exception {
		Menu menu=(Menu)this.menuDomain.query(menuId);
		String parentId=menu.getParentId();
		DetachedCriteria criteria=baseDomain.getCriteria(null);
		criteria.add(Restrictions.eq("roleId", roleId));
		criteria.add(Restrictions.eq("menuId", menuId));
		List list=baseDomain.queryByCriteria(criteria);
		if(list.size()==0){
			MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
			entity.setMenuId(menuId);
			entity.setRoleId(roleId);
			entity.setValid(true);
			if(!rootValue.equals(parentId)){
				updateparent(request,parentId,roleId);
			}
			entity.setTname(menu.getTname());
			this.saveBaseInfoToObject(request, entity);
			this.baseDomain.save(entity);
		}else if(list.size()==1){
			MenuPrivilege entity=(MenuPrivilege)list.get(0);
			entity.setValid(true);
			if(!rootValue.equals(parentId)){
				updateparent(request,parentId,roleId);
			}
			this.baseDomain.update(entity);
		}else{
			MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
			entity.setMenuId(menuId);
			entity.setRoleId(roleId);
			entity.setValid(true);
			entity.setTname(menu.getTname());
			this.saveBaseInfoToObject(request, entity);
			List saveList=new ArrayList();
			saveList.add(entity);
			this.baseDomain.deleteAndSaveAndUpdate(list, saveList,null);
		}
	}
	/***
	 * ajax批量更新授权
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void jsupdates(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<String> roleids=HttpUtil.getList(request, "roleids", ";");
		String menuId=HttpUtil.getString(request, "menuId", "");
		Boolean result=HttpUtil.getBoolean(request, "result", true);
		try{
			if(roleids.size()>0&&!menuId.equals("")){
				List saveList=new ArrayList();
				List updateList=new ArrayList();
				List deleteList=new ArrayList();
				Menu menu=(Menu) menuDomain.query(menuId);
				String url=menu.getUrl();
				String defaultAction="list";
				Appes appes=null;
				if(url.indexOf(".do")>-1){//有针对的控制器应用
					String appesId=url.substring(0, url.indexOf(".do"));
					if(url.indexOf("action")>-1){
						defaultAction=url.substring(url.indexOf("action")+6, url.length());
						if(defaultAction.indexOf("&")>0){
							defaultAction=defaultAction.substring(0,url.indexOf("&")).trim();
						}else{
							defaultAction=defaultAction.trim();
						}
					}
					appes=appesDomain.getAppes(appesId);
				}
				for(String roleId:roleids){
					DetachedCriteria criteria=baseDomain.getCriteria(null);
					criteria.add(Restrictions.eq("roleId", roleId));
					criteria.add(Restrictions.eq("menuId", menuId));
					List list=baseDomain.queryByCriteria(criteria);
					if(list.size()==0){
						MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
						entity.setMenuId(menuId);
						entity.setRoleId(roleId);
						entity.setValid(result);
						entity.setTname(menu.getTname());
						this.saveBaseInfoToObject(request, entity);
						saveList.add(entity);
					}else if(list.size()==1){
						MenuPrivilege entity=(MenuPrivilege)list.get(0);
						entity.setValid(result);
						updateList.add(entity);
					}else{
						deleteList.addAll(list);
						MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
						entity.setMenuId(menuId);
						entity.setRoleId(roleId);
						entity.setValid(result);
						entity.setTname(menu.getTname());
						this.saveBaseInfoToObject(request, entity);
						saveList.add(entity);
					}
					if(appes!=null&&appes.getActionList().size()>0){
						if(result){
							appesActionPrivilegeDomain.addAllPrivilegeForRole(appes, roleId, defaultAction);
						}else{
							appesActionPrivilegeDomain.deletePrivilegeForRole(appes, roleId, defaultAction);
						}
					}
				}
				this.baseDomain.deleteAndSaveAndUpdate(deleteList, saveList,updateList);
				this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
			}else{
				this.outFailString(request,response, "更新失败！","");
			}
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, "更新失败！","");
		}
	}
	/***
	 * ajax批量更新授权
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void jsupdatesbyrole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<String> menuIds =HttpUtil.getList(request, "menuIds", ";");
		String roleId=HttpUtil.getString(request, "roleId", "");
		Boolean result=HttpUtil.getBoolean(request, "result", true);
		try{
			if(menuIds.size()>0&&!roleId.equals("")){
				List saveList=new ArrayList();
				List updateList=new ArrayList();
				List deleteList=new ArrayList();
				for(String menuId:menuIds){
					DetachedCriteria criteria=baseDomain.getCriteria(null);
					criteria.add(Restrictions.eq("roleId", roleId));
					criteria.add(Restrictions.eq("menuId", menuId));
					List list=baseDomain.queryByCriteria(criteria);
					if(list.size()==0){
						MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
						Menu menu=(Menu)this.menuDomain.query(menuId);
						entity.setMenuId(menuId);
						entity.setRoleId(roleId);
						entity.setValid(result);
						entity.setTname(menu.getTname());
						this.saveBaseInfoToObject(request, entity);
						saveList.add(entity);
					}else if(list.size()==1){
						MenuPrivilege entity=(MenuPrivilege)list.get(0);
						entity.setValid(result);
						updateList.add(entity);
					}else{
						deleteList.addAll(list);
						MenuPrivilege entity=(MenuPrivilege)baseDomain.getBaseObject();
						Menu menu=(Menu)this.menuDomain.query(menuId);
						entity.setMenuId(menuId);
						entity.setRoleId(roleId);
						entity.setValid(result);
						entity.setTname(menu.getTname());
						this.saveBaseInfoToObject(request, entity);
						saveList.add(entity);
					}
				}
				this.baseDomain.deleteAndSaveAndUpdate(deleteList, saveList,updateList);
				this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
			}else{
				this.outFailString(request,response, "更新失败！","");
			}
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, "更新失败！","");
		}
		
	}
	public void setMenuDomain(MenuDomain menuDomain) {
		this.menuDomain = menuDomain;
	}
	public void setRoleDomain(RoleDomain roleDomain) {
		this.roleDomain = roleDomain;
	}
	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}
	public void setAppesActionPrivilegeDomain(
			AppesActionPrivilegeDomain appesActionPrivilegeDomain) {
		this.appesActionPrivilegeDomain = appesActionPrivilegeDomain;
	}
}
