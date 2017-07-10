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

import com.starsoft.core.domain.RoleDomain;
import com.starsoft.core.entity.AppesActionPrivilege;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class AppesActionPrivilegeController extends BaseAjaxController implements BaseInterface {
	private RoleDomain roleDomain;
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
		String appes=HttpUtil.getString(request, "appes","");
		String actionName=HttpUtil.getString(request, "actionName","");
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		criteria.add(Restrictions.eq("appes", appes));
		criteria.add(Restrictions.eq("tname", actionName));
		List selectlist= baseDomain.queryByCriteria(criteria, page);
		String roleTname=HttpUtil.getString(request, "tname","");
		List roleList = null;
		if(roleTname.equals("")){//按角色搜索
			roleList = roleDomain.queryAll();
		}else {
			roleList = roleDomain.queryByProperty("tname", roleTname);
		}
		model.put("list", roleList);
		model.put("selectlist", selectlist);
		model.put("page", page);
		return new ModelAndView(this.getListPage(),model);
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
		return new ModelAndView(this.getAddPage(),model);
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
		String method=request.getMethod().toLowerCase();
		String gotourl=request.getRequestURI();
		if("post".equals(method)){
			try{
				BaseObject entity=baseDomain.getBaseObject();
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
		gotourl=request.getRequestURI()+"?page="+page;
		if("post".equals(method)){
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
	public void changePrivilege(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> roleIds=HttpUtil.getList(request, "roleIds", ";");
		String appes=HttpUtil.getString(request, "appes","");
		String actionName=HttpUtil.getString(request, "actionName","");
		boolean result=HttpUtil.getBoolean(request, "result",true);
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			if(roleIds.size()>0){
				if(result){//添加权限
					List deleteList=new ArrayList();
					List saveList=new ArrayList();
					//删除原来的
					DetachedCriteria criteria = this.convertCriteria(request);
					criteria.add(Restrictions.eq("appes", appes));
					criteria.add(Restrictions.eq("tname", actionName));
					criteria.add(Restrictions.in("roleId", roleIds));
					deleteList= baseDomain.queryByCriteria(criteria);
					for(String role:roleIds){
						AppesActionPrivilege newAppesActionP=new AppesActionPrivilege();
						newAppesActionP.setAppes(appes);
						newAppesActionP.setRoleId(role);
						newAppesActionP.setTname(actionName);
						this.saveBaseInfoToObject(request, newAppesActionP);
						saveList.add(newAppesActionP);
					}
					baseDomain.deleteAndSaveAndUpdate(deleteList, saveList, null);
				}else{
					List deleteList=new ArrayList();
					//删除原来的
					DetachedCriteria criteria = this.convertCriteria(request);
					criteria.add(Restrictions.eq("appes", appes));
					criteria.add(Restrictions.eq("tname", actionName));
					criteria.add(Restrictions.in("roleId", roleIds));
					deleteList= baseDomain.queryByCriteria(criteria);
					baseDomain.deleteAndSaveAndUpdate(deleteList, null, null);
				}
			}
			this.outSuccessString(request,response,"");
		}else{
			this.outFailString(request,response, "对不起您没有操作权限 !","");
		}
	}
	public void setRoleDomain(RoleDomain roleDomain) {
		this.roleDomain = roleDomain;
	}
}
