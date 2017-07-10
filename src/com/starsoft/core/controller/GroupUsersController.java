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

import com.starsoft.core.domain.GroupDomain;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Group;
import com.starsoft.core.entity.GroupUsers;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;


public class GroupUsersController extends BaseAjaxController implements BaseInterface {
	private OrganDomain organDomain;
	private GroupDomain groupDomain;
	private UsersDomain usersDomain;
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
		String groupId=HttpUtil.getString(request, "groupId", "");
		DetachedCriteria criteria = this.convertCriteria(request);
		if(!groupId.equals("")){
			criteria.add(Restrictions.eq("groupId", groupId));
			Group group=(Group)groupDomain.query(groupId);
			if(group!=null){
				model.put("groupName", group.getTname());
			}
		}
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		model.put("groupId", groupId);
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
		String gotourl="";
		BaseObject entity=baseDomain.getBaseObject();
		String method=request.getMethod().toLowerCase();
		gotourl=request.getRequestURI();
		if("post".equals(method)){
			try{
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
		String groupId=HttpUtil.getString(request, "groupId", "");
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
		if(!groupId.equals("")){
			selectUsers=this.baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("groupId", groupId)));
		}
		model.put("selectUsers", selectUsers);
		model.put("list", list);
		model.put("groupId", groupId);
		model.put("rightContent", "groupUsers.do?action=selectlist&organId="+parentId);
		model.put("urlLink", "groupUsers.do?action=selectlist&organId=");
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
		String groupId=HttpUtil.getString(request, "groupId", "");
		String userIdp=HttpUtil.getString(request, "userIds", "");
		try{
			List result=new ArrayList();
			List userIds=StringUtil.toList(userIdp, ";");
			for(int t=0;t<userIds.size();t++){
				String userId=userIds.get(t).toString();
				DetachedCriteria criteria = baseDomain.getCriteria(null);
				criteria.add(Restrictions.eq("groupId", groupId));
				criteria.add(Restrictions.eq("usersId", userId));
				List list= baseDomain.queryByCriteria(criteria);
				if(list.size()==0){
					GroupUsers groupUsers=(GroupUsers)baseDomain.getBaseObject();
					Group group=new Group();
					group.setId(groupId);
					Users users=(Users)this.usersDomain.query(userId);
					users.setId(userId);
					groupUsers.setGroupId(groupId);
					groupUsers.setUsersId(userId);
					groupUsers.setTname(users.getTname());
					this.bind(request, groupUsers);
					this.saveBaseInfoToObject(request, groupUsers);
					groupUsers.setValid(true);
					result.add(groupUsers);
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
		String groupId=HttpUtil.getString(request, "groupId","");
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)&&!groupId.equals("")){
			List list=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("groupId", groupId)).add(Restrictions.in("id", ids)));
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
	public void setOrganDomain(OrganDomain organDomain) {
		this.organDomain = organDomain;
	}
	public void setGroupDomain(GroupDomain groupDomain) {
		this.groupDomain = groupDomain;
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
}
