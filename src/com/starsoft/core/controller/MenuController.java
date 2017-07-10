package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.starsoft.core.domain.AppesActionPrivilegeDomain;
import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.domain.MenuDomain;
import com.starsoft.core.domain.MenuPrivilegeDomain;
import com.starsoft.core.domain.RoleDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.AppesActionPrivilege;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.Menu;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class MenuController extends BaseTreeController implements BaseInterface {
	private MenuPrivilegeDomain menuPrivilegeDomain;
	private AppesDomain appesDomain;
	private RoleDomain roleDomain;
	private AppesActionPrivilegeDomain appesActionPrivilegeDomain;
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
		if(user!=null&&"admin".equals(user.getId())){
			list=((MenuDomain)baseDomain).queryTreeByParentId(parentId, 6, null, false);
		}else if(roleIds!=null&&roleIds.size()>0){
			list=((MenuDomain)baseDomain).querySubTreesByParentIdAndRoles(parentId, roleIds);
		}
		model.put("list", list);
		model.put("title", "菜单管理");
		model.put("deleteAndAdd", "true");// 添加节点和删除节点的权利
		model.put("rightContent", "?action=add");
		model.put("urlLink", "?action=read&id=");
		model.put("addLink", "menu.do?action=add&parentId=");
		return new ModelAndView(this.getCustomPage("subframetree"),model);
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
	public ModelAndView privateMenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String id=HttpUtil.getString(request, "id","");
		String password=HttpUtil.getString(request, "password","");
		return new ModelAndView("baseframe/indexleft", model);
	}
	public ModelAndView menutree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String parentId=HttpUtil.getString(request,"parentId", rootValue);
		List<String> roleIds= (List<String>) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS);
		BaseObject user=HttpUtil.getLoginUser(request);
		List list=new ArrayList();
		if(user.getId().equals("admin")){
			list=((MenuDomain)baseDomain).queryTreeByParentId(parentId, 3, true, false);
		}else{
			list=((MenuDomain)baseDomain).querySubTreesByParentIdAndRoles(parentId, roleIds);
		}
		model.put("list", list);
		return new ModelAndView("baseframe/menuleft", model);
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
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		Page page=HttpUtil.convertPage(request);
		List list=new ArrayList();
		List<String> roleIds= (List<String>) WebUtils.getSessionAttribute(request, WEBCONSTANTS.SESSION_ROLEIDS);
		BaseObject user=HttpUtil.getLoginUser(request);
		if(user.getId().equals("admin")){
			list=((MenuDomain)baseDomain).queryTreeByParentId(parentId, 1, true, false);
		}else{
			list=((MenuDomain)baseDomain).querySubTreesByParentIdAndRoles(parentId, roleIds);
		}
		if(list.size()>0){
			page.setPageSize(list.size());
		}
		page.setTotalCount(list.size());
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getListPage(),model);
	}
	
	/***
	 * 新建方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = HttpUtil.convertModel(request);
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		List parentlist=menuDomain.queryTreeByParentId(parentId, 1, true, false);
		model.put("parentlist", parentlist);	
		model.put("parentId", parentId);		
		return new ModelAndView(this.getAddPage(),model);
	}
	public void save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gotourl="";
		try{
			Menu entity=(Menu) baseDomain.getBaseObject();
			this.bind(request, entity);
			this.saveBaseInfoToObject(request, entity);
			this.baseDomain.save(entity);
			gotourl=request.getRequestURI()+"?parentId="+entity.getParentId();
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
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
		Menu obj=(Menu) baseDomain.query(id);
		boolean hasapp=false;
		if(obj!=null){
//			List parentlist=menuDomain.queryTreeByParentId(null, 4, true, false);
			List parentlist=menuDomain.queryTreeByBaseObjectIdNotContainId(rootValue, id, 4, true, false);
			model.put("parentlist", parentlist);	
			model.put("obj", obj);
			model.put("page", page);
			List rolelist=roleDomain.queryAll();
			model.put("rolelist", rolelist);
			DetachedCriteria criteria=menuPrivilegeDomain.getCriteria(true);
			criteria.add(Restrictions.eq("menuId", id));
			List selectlist= baseDomain.queryByCriteria(criteria);
			model.put("selectlist", selectlist);
			List selectPrivilegeList=new ArrayList();
			String url=obj.getUrl();
			if(url!=null&&url.indexOf(".do")>-1){
				String appesId=url.substring(0, url.indexOf(".do"));
				model.put("appesId", appesId);
				Appes appes=appesDomain.getAppes(appesId);
				if(appes!=null&&appes.getActionList().size()>0){
					hasapp=true;
					model.put("appes", appes);
					model.put("actionList", appes.getActionList());
					
				}
				DetachedCriteria aapcriteria=DetachedCriteria.forClass(AppesActionPrivilege.class);
				aapcriteria.add(Restrictions.eq("appes", appesId));
				selectPrivilegeList=appesActionPrivilegeDomain.queryByCriteria(aapcriteria);
			}
			model.put("selectPrivilegeList", selectPrivilegeList);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		model.put("hasapp", hasapp);
		return new ModelAndView(this.getEditPage(),model);
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
			Menu entity=(Menu) baseDomain.query(id);
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
	/**
	 * 彻底删除菜单对象功能,包含删除子菜单
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
//				menuPrivilegeDomain.deletes("menuId", ids);
//				baseDomain.deletes(ids);
				for(String id:ids){
					List menuId=new ArrayList();
					menuId.add(id);
					List<BaseTreeObject> list1=menuDomain.queryTreeByParentId(id, 3, null, true);
					for(BaseTreeObject base1:list1){
						menuId.add(base1.getId());
						List<BaseTreeObject> list2=base1.getSubset();
						for(BaseTreeObject base2:list2){
							menuId.add(base2.getId());
							List<BaseTreeObject> list3=base2.getSubset();
							for(BaseTreeObject base3:list3){
								menuId.add(base3.getId());
							}
						}
					}
					menuPrivilegeDomain.deletes("menuId", menuId);
					baseDomain.deletes(menuId);
				}
				this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
			}catch(Exception e){
				String msg="数据已经被使用，不能删除!";
				this.outFailString(request,response,msg,"");
			}
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
		}
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map model=HttpUtil.convertModel(request);
		List listImages=new ArrayList();
		for(int t=1;t<49;t++){
			if(t<10){
				listImages.add("0"+t+".png");
			}else{
				listImages.add(t+".png");
			}
		}
		model.put("listImages", listImages);
		return new ModelAndView(this.getCustomPage("selectImage"),model);
	}
	public void setRoleDomain(RoleDomain roleDomain) {
		this.roleDomain = roleDomain;
	}
	public void setMenuPrivilegeDomain(MenuPrivilegeDomain menuPrivilegeDomain) {
		this.menuPrivilegeDomain = menuPrivilegeDomain;
	}
	public void setAppesActionPrivilegeDomain(
			AppesActionPrivilegeDomain appesActionPrivilegeDomain) {
		this.appesActionPrivilegeDomain = appesActionPrivilegeDomain;
	}
	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}
	public void setMenuDomain(MenuDomain menuDomain) {
		this.menuDomain = menuDomain;
		this.setBaseTreeDomain(menuDomain);
	}
}
