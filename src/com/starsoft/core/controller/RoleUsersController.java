package com.starsoft.core.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.RoleDomain;
import com.starsoft.core.domain.RoleUsersDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.Role;
import com.starsoft.core.entity.RoleUsers;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;

public class RoleUsersController extends BaseAjaxController implements BaseInterface {
	private RoleDomain roleDomain;
	private UsersDomain usersDomain;
	private OrganDomain organDomain;
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
		String roleId=HttpUtil.getString(request, "roleId", "");
		List list=new ArrayList();
		if(!roleId.equals("")){
			Role role=(Role) roleDomain.query(roleId);
			if(role!=null){
				model.put("roleName", role.getTname());
				List parameters=new ArrayList();
				parameters.add(roleId);
				String hql="select users from com.starsoft.core.entity.Users as users where users.valid=true and users.id in(select ru.usersId from com.starsoft.core.entity.RoleUsers as ru where ru.roleId=?)";
				list=this.baseDomain.queryByHql(hql, parameters,page);
			}else{
				model.put("roleName", "角色不存在");
			}
		}else{
			model.put("roleName", "未传递角色信息");
		}
		model.put("list", list);
		model.put("page", page);
		model.put("roleId", roleId);
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
		String userId=HttpUtil.getString(request, "userId", "");
		List rolelistall=roleDomain.queryAll();
		List rolelist=new ArrayList();
		List selectRoleUserList=baseDomain.queryByProperty("usersId", userId);
		StringBuffer selectroleids=new StringBuffer();
		List<String> roleIds=new ArrayList<String>();
		for(int t=0;t<selectRoleUserList.size();t++){
			RoleUsers roleUsers=(RoleUsers)selectRoleUserList.get(t);
			selectroleids.append(roleUsers.getRoleId());
			roleIds.add(roleUsers.getRoleId());
		}
		for(int t=0;t<rolelistall.size();t++){
			Role role=(Role)rolelistall.get(t);
			if(selectroleids.indexOf(role.getId())==-1){
				rolelist.add(role);
			}
		}
		DetachedCriteria criteria=roleDomain.getCriteria(null);
		List selectList=new ArrayList();
		if(roleIds.size()>0){
			criteria.add(Restrictions.in("id", roleIds));
			selectList=roleDomain.queryByCriteria(criteria);
			model.put("selectList", selectList);
		}else{
			model.put("selectList", selectList);
		}
		model.put("rolelist", rolelist);
		model.put("userIdTemp", userId);
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
		Users user = HttpUtil.getLoginUser(request);
		String userId=HttpUtil.getString(request, "userIds", "");
		String resultlist=HttpUtil.getString(request, "roleId", ",");
		String gotourl=request.getRequestURI();
		List<String> userIdList = new ArrayList<String>();
		if(!"".equals(userId))
		{
			String[] strarray=userId.split(";");
			userIdList = Arrays.asList(strarray);
		}
		try{
			((RoleUsersDomain)baseDomain).updateRoleUsers(userIdList, resultlist,user.getOrganId());
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
	 * 选择用户页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectUsers(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String parentId=HttpUtil.getString(request, "parentId", "");
		String roleId=HttpUtil.getString(request, "roleId", "");
		if(parentId.equals("")){
			Users user=HttpUtil.getLoginUser(request);
			parentId=user.getOrganId();
			if(!"admin".equals(user.getId())){
				String administratorOrganId=organDomain.queryTopAdministratorOrgan(user.getId());
				if(!administratorOrganId.equals("")){
					if(!administratorOrganId.equals(parentId)){
						parentId=administratorOrganId;
					}
				}
			}else{
				parentId=rootValue;
			}
		}
		List list=new ArrayList();
		list.add(organDomain.queryTreeByBaseObjectId(parentId,5,true,false));
		List selectUsers=new ArrayList();
		if(!roleId.equals("")){
			selectUsers=this.baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("roleId", roleId)));
		}
		model.put("selectUsers", selectUsers);
		model.put("list", list);
		model.put("roleId", roleId);
		model.put("rightContent", "roleUsers.do?action=selectlist&organId="+parentId);
		model.put("urlLink", "roleUsers.do?action=selectlist&organId=");
		return new ModelAndView(this.getCustomPage("select"),model);
	}
	/***
	 * 选择列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		Page page= HttpUtil.convertPage(request);
		
		String organId=HttpUtil.getString(request, "organId", rootValue);
		DetachedCriteria criteria = DetachedCriteria.forClass(Users.class);
		criteria.add(Restrictions.eq("valid", true));
		criteria.add(Restrictions.eq("organId", organId));
		String name=HttpUtil.getString(request, "tname","");
		if(!name.equals("")){
			criteria.add(Restrictions.ilike("tname", "%"+name+"%"));
		}
		List list= baseDomain.queryByCriteria(criteria, page);
		Organ organ=(Organ)organDomain.query(organId);
		model.put("list", list);
		model.put("page", page);
		model.put("organ", organ);
		return new ModelAndView(this.getCustomPage("selectlist"),model);
	}
	/***
	 * 更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void pathupdate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String roleId=HttpUtil.getString(request, "roleId", "");
		String userIdp=HttpUtil.getString(request, "userIds", "");
		try{
			List result=new ArrayList();
			List userIds=StringUtil.toList(userIdp, ";");
			for(int t=0;t<userIds.size();t++){
				String userId=userIds.get(t).toString();
				DetachedCriteria criteria = baseDomain.getCriteria(null);
				criteria.add(Restrictions.eq("roleId", roleId));
				criteria.add(Restrictions.eq("usersId", userId));
				List list= baseDomain.queryByCriteria(criteria);
				if(list.size()==0){
					RoleUsers roleUsers=(RoleUsers)baseDomain.getBaseObject();
					Role role=new Role();
					role.setId(roleId);
					Users users=(Users)this.usersDomain.query(userId);
					users.setId(userId);
					roleUsers.setRoleId(roleId);
					roleUsers.setUsersId(userId);
					roleUsers.setTname(users.getTname());
					this.bind(request, roleUsers);
					roleUsers.setValid(true);
					result.add(roleUsers);
				}
			}
			if(result.size()>0){
				this.baseDomain.save(result);
			}
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, "保存数据出现异常","");
		}
	}
	/**
	 * 彻底删除对象功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String roleId=HttpUtil.getString(request, "roleId","");
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)&&!roleId.equals("")){
			List list=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("roleId", roleId)).add(Restrictions.in("usersId", ids)));
			try{
				baseDomain.deleteAndSaveAndUpdate(list, null, null);
			}catch(Exception e){
				String msg="数据已经被使用，不能删除!";
				this.outFailString(request,response,msg,"");
			}
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
		}
	}
	public void setRoleDomain(RoleDomain roleDomain) {
		this.roleDomain = roleDomain;
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
	public void setOrganDomain(OrganDomain organDomain) {
		this.organDomain = organDomain;
	}
}
