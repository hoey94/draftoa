package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.domain.JobPostDomain;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.RoleDomain;
import com.starsoft.core.domain.RoleUsersDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.AppesAttribute;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.GroupUsers;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.OrganAdministrator;
import com.starsoft.core.entity.Role;
import com.starsoft.core.entity.RoleUsers;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HashUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.Pinyin4jUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.FileUpload;

public class UsersController extends BaseTreeController implements BaseInterface {
	private OrganDomain organDomain;
	private RoleUsersDomain roleUsersDomain;
	private JobPostDomain jobPostDomain;
	private RoleDomain roleDomain;
	private UsersDomain usersDomain;
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
		String parentId=user.getOrganId();
		List list=new ArrayList();
		if("admin".equals(user.getId())){//超级管理员管理所有的部门信息
			List lists= organDomain.queryTreeByParentId(parentId,1,null,false);
			list.addAll(lists);
			model.put("list", list);
			model.put("title", "组织部门");
			model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
			model.put("rightContent", "users.do?action=list&sortfield=sortCode&sortvalue=false");
			model.put("urlLink", "users.do?action=list&organId=");
			model.put("addLink", "users.do?action=add&organId=");
			return new ModelAndView("baseframe/subframetree",model);
		}else{
			String administratorOrganId=organDomain.queryTopAdministratorOrgan(user.getId());
			if(!administratorOrganId.equals("")){
				if(!administratorOrganId.equals(parentId)){
					parentId=administratorOrganId;
				}
				if(!parentId.equals(rootValue)){
					BaseTreeObject topNode=(BaseTreeObject) organDomain.query(parentId);
					if(topNode!=null){
						topNode.setParentId(rootValue);
						topNode.setSubset(organDomain.queryTreeByParentId(parentId,1,null,false));
						list.add(topNode);
					}else{
						topNode=new Organ();
						topNode.setParentId(rootValue);
						topNode.setId(parentId);
						topNode.setTname("未知节点");
						topNode.setSubset(organDomain.queryTreeByParentId(parentId,1,null,false));
						list.add(topNode);
					}
				}else{
					List lists= organDomain.queryTreeByParentId(parentId,1,null,false);
					list.addAll(lists);
				}
				
				model.put("list", list);
				model.put("title", "组织部门");
				model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
				model.put("rightContent", "users.do?action=list&sortfield=sortCode&sortvalue=false");
				model.put("urlLink", "users.do?action=read&organId=");
				model.put("addLink", "users.do?action=add&organId=");
				return new ModelAndView("baseframe/subframetree",model);
			}else{
				model.put("list", list);
				model.put("title", "组织部门");
				model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
				model.put("rightContent", "?action=list&sortfield=sortCode&sortvalue=false");
				model.put("urlLink", "users.do?action=read&organId=");
				model.put("addLink", "users.do?action=add&organId=");
				return new ModelAndView("baseframe/subframetree",model);
			}
		}
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
		String organId = HttpUtil.getString(request, "organId","");
		String userId = HttpUtil.getString(request, "userId","");
		if(!organId.equals("")){
			criteria.add(Restrictions.eq("organId", organId));
			model.put("organId", organId);
			Organ organ=(Organ)organDomain.query(organId);
			if(organ!=null){
				model.put("organName", organ.getTname());
			}
		}else{//如果未传递ID则显示可以显示的人员
			Users user=HttpUtil.getLoginUser(request);
			if(!"admin".equals(user.getId())){
				String administratorOrganId=organDomain.queryTopAdministratorOrgan(user.getId());
				String parentId=user.getOrganId();
				List listOrganIds=new ArrayList();
				listOrganIds.add(administratorOrganId);
				if(!administratorOrganId.equals("")){
					if(!administratorOrganId.equals(parentId)){
						parentId=administratorOrganId;
					}
					if(!parentId.equals(rootValue)){
						BaseTreeObject topNode=(BaseTreeObject) organDomain.query(parentId);
						if(topNode!=null){
							topNode.setParentId(rootValue);
							listOrganIds.add(topNode.getParentId());
						}
					}
					List<BaseTreeObject> lists= organDomain.queryTreeByParentId(parentId,1,null,false);
					for(BaseTreeObject baseTreeObject:lists){
						listOrganIds.add(baseTreeObject.getId());
						List<BaseTreeObject> sublists=baseTreeObject.getSubset();
						for(BaseTreeObject subaseTreeObject:sublists){
							listOrganIds.add(subaseTreeObject.getId());
							List<BaseTreeObject> subslists=subaseTreeObject.getSubset();
							for(BaseTreeObject susbaseTreeObject:subslists){
								listOrganIds.add(susbaseTreeObject.getId());
							}
						}
					}
				}
				if(listOrganIds.size()==0){
					listOrganIds.add("K"+rootValue);
				}
				criteria.add(Restrictions.in("organId", listOrganIds));
			}
		}
		criteria.add(Restrictions.ne("id", "admin"));
		if(!userId.equals("")){
			criteria.add(Restrictions.ilike("id", userId, MatchMode.ANYWHERE));
		}
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		model.put("userId", userId);
		return new ModelAndView(this.getListPage(),model);
	}
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
		String sortfield =HttpUtil.getString(request, "sortfield","sortCode");
		boolean sortValue=HttpUtil.getBoolean(request, "sortvalue", true);
		if(sortValue){
			criteria.addOrder(Order.desc(sortfield));
		}else{
			criteria.addOrder(Order.asc(sortfield));
		}
		return criteria;
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
		String organId = HttpUtil.getString(request, "organId","");
		if(!organId.equals("")){//显示一个部门的用户列表信息
			return new ModelAndView("redirect:/users.do?organId="+organId);
		}else{
			ModelAndView model=this.edit(request, response);
			return new ModelAndView(this.getReadPage(),model.getModel());
		}
	}
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView readlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		String organId = HttpUtil.getString(request, "organId","");
		if(!organId.equals("")){
			criteria.add(Restrictions.eq("organId", organId));
			model.put("organId", organId);
		}
		criteria.add(Restrictions.ne("id", "admin"));
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("readlist"),model);
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
		Users user=HttpUtil.getLoginUser(request);
		List organlist=new ArrayList();
		String parentId=user.getOrganId();
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
		organlist.add(organDomain.queryTreeByBaseObjectId(parentId, 4, true, false));
		model.put("organlist", organlist);
		List jobpostList=jobPostDomain.queryByCriteria(jobPostDomain.getCriteria(true));
		model.put("jobpostList", jobpostList);
		String organId = HttpUtil.getString(request, "organId","");
		model.put("organId", organId);
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
		Users user=HttpUtil.getLoginUser(request);
		String parentId=user.getOrganId();
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
		Map<String,Object> model = new HashMap<String,Object>();
		Users obj=(Users) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			//查询所属学校
			if(!"".equals(obj.getOrganId())){
			Organ organ=(Organ)organDomain.query(obj.getOrganId());
			if(organ!=null){
				model.put("organ", organ.getTname());
			}
			}
			model.put("page", page);
			List organlist=new ArrayList();
			organlist.add(organDomain.queryTreeByBaseObjectId(parentId, 4, true, false));
			model.put("organlist", organlist);
			model.put("organId", obj.getOrganId());
			List jobpostList=jobPostDomain.queryByCriteria(jobPostDomain.getCriteria(true));
			model.put("jobpostList", jobpostList);
			List roleIds=roleUsersDomain.getRoleIdsByUserId(id);
			List rolelist=new ArrayList();
			if(roleIds.size()>0){
				DetachedCriteria criteria=DetachedCriteria.forClass(Role.class);
				criteria.add(Restrictions.in("id", roleIds));
				rolelist=roleUsersDomain.queryByCriteria(criteria);
			}
			model.put("rolelist", rolelist);
			model.put("roleIds", roleIds);
			List allrole=roleDomain.queryAll();
			model.put("allrole", allrole);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		return new ModelAndView(this.getEditPage(),model);
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView modify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject obj=(BaseObject) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			model.put("page", page);
			List organlist=organDomain.queryTreeByParentId(rootValue, 4, true, false);
			model.put("organlist", organlist);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(SubCloseMessagePage,model);
		}
		return new ModelAndView(this.getCustomPage("modify"),model);
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
		String id=HttpUtil.getString(request, "id", "");
		String gotourl=HttpUtil.getString(request, "gotourl", "");
		Users entity=(Users)baseDomain.getBaseObject();
		try{
			this.bind(request, entity);
			Object obj=baseDomain.query(id);
			if(obj!=null){
				this.outFailString(request,response, "登录账户已经被占用！请重新设置登录帐号！","");
			}else{
				this.saveBaseInfoToObject(request, entity);
				entity.setPassword(HashUtil.toMD5(entity.getPassword()));
				String pingyin=Pinyin4jUtil.getPinYinHeadChar(entity.getTname());
				entity.setQueryCode(pingyin);
				Users loginUser=HttpUtil.getLoginUser(request);
				entity.setOrganId(loginUser.getOrganId());
				entity.setCreateId(loginUser.getId());
				this.baseDomain.save(entity);
				//增加默认普通人员角色
				RoleUsers roleUsers = new RoleUsers();
				roleUsers.setUsersId(entity.getId());
				roleUsers.setRoleId("RCOMMONUSER");
				roleUsers.setValid(true);
				roleUsers.setTname(entity.getTname());
				this.usersDomain.save(roleUsers);
				this.outSuccessString(request,response, gotourl);
			}
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
		if(id.equals("")){
			this.outFailString(request,response, "用户唯一标识不能为空！",gotourl);
		}else if("admin".equals(id)){
			this.outFailString(request,response, "超级管理员不可以编辑",gotourl);
		}else{
			try{
				Users entity=(Users) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				String pingyin=Pinyin4jUtil.getPinYinHeadChar(entity.getTname());
				entity.setQueryCode(pingyin);
				this.baseDomain.update(entity);
				gotourl=gotourl+"&organId="+entity.getOrganId();
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}
	}
	/****
	 * 获取可用帐号
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getAccount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tname=HttpUtil.getString(request, "tname", "");
		if(!tname.equals("")){
			String pingyin=Pinyin4jUtil.getPinYin(tname);
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
	public void modifyupdate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String id=HttpUtil.getString(request, "id", "");
		String password1=HttpUtil.getString(request, "password1", "");
		String password2=HttpUtil.getString(request, "password2", "");
		Users entity=(Users) baseDomain.query(id);
		try{
			if(entity!=null){
				this.bind(request, entity);
			}
			if(!password1.equals("")&&!password1.equals("")&&password1.equals(password2)){
				entity.setPassword(HashUtil.toMD5(password1));
				this.baseDomain.update(entity);
				this.outSuccessString(request,response, "");
			}else{
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,"");
			}
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,"");
		}
	}
	/****
	 * 密码重置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void resetpassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		Users entity=(Users) baseDomain.query(id);
		if(entity!=null){
			entity.setPassword(HashUtil.toMD5("123456"));
			baseDomain.update(entity);
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
		}else{
			this.outFailString(request,response, "选择的用户不存在！","");
		}
	}
	/***
	 *公用选择人员功能
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectUsers(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		String parentId = HttpUtil.getString(request, "parentId",rootValue);
		String defaultOragan = HttpUtil.getString(request, "organId","");
		String selecttype = HttpUtil.getString(request, "selecttype","0");//0代表多选 1代表单选
		String levelzige = HttpUtil.getString(request, "levelzige","");//0代表多选 1代表单选
		List<BaseTreeObject> list = new ArrayList<BaseTreeObject>();
		BaseTreeObject organ=this.organDomain.queryTreeByBaseObjectId(parentId,5,Boolean.valueOf(true), Boolean.valueOf(false));
		if(organ!=null&&!rootValue.equals(parentId)){
			organ.setParentId(rootValue);
		}
		if(organ!=null){
			list.add(organ);
			model.put("organ", organ);
		}else{
			BaseTreeObject topNode=new Organ();
			topNode.setParentId(rootValue);
			topNode.setId(parentId);
			topNode.setTname("未知节点");
			topNode.setSubset(organDomain.queryTreeByParentId(parentId,1,null,false));
			list.add(topNode);
			model.put("organ", topNode);
		}
		if(selecttype.equals("0")){//多选
			List selectUsers = new ArrayList();
			List selectIds = HttpUtil.getList(request, "selectUsersIds", ";");
			if (selectIds.size() > 0)
				selectUsers = this.baseDomain.queryByCriteria(this.baseDomain
						.getCriteria(Boolean.valueOf(true)).add(
								Restrictions.in("id", selectIds)));
			model.put("selectUsers", selectUsers);
			model.put("organlist", list);
			model.put("rightContent", "users.do?action=selectlist&organId="
					+ parentId+"&levelzige="+levelzige);
			model.put("urlLink", "users.do?action=selectlist&levelzige="+levelzige+"&organId=");
			return new ModelAndView(getCustomPage("select"), model);
		}else{
			String selectId=HttpUtil.getString(request, "selectUsersIds","");
			if(selectId.indexOf(";")>-1){
				selectId=selectId.substring(0,selectId.indexOf(";"));
			}
			defaultOragan = parentId;
			model.put("rightContent", "users.do?action=selectsinglelist&selecttype=1&organId="
					+ defaultOragan+"&levelzige="+levelzige);
			model.put("organlist", list);
			model.put("urlLink", "users.do?action=selectsinglelist&levelzige="+levelzige+"&selecttype=1&organId=");
			model.put("selectId", selectId);
			return new ModelAndView(getCustomPage("singleselect"), model);
		}
	}
	/***
	 * 用户选择列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map model = HttpUtil.convertModel(request);
		Page page = HttpUtil.convertPage(request);
		String organId = HttpUtil.getString(request, "organId",
				"11111111111111111111111111111111");
		String duty=HttpUtil.getString(request, "duty","");
		DetachedCriteria criteria = this.baseDomain.getCriteria(null);
		criteria.add(Restrictions.eq("organId", organId));
		criteria.add(Restrictions.eq("duty", duty));
		String name = HttpUtil.getString(request, "tname", "");
		if (!(name.equals(""))){
			criteria.add(Restrictions.ilike("tname", "%" + name + "%"));
		}
		List list = this.baseDomain.queryByCriteria(criteria, page);
		Organ organ = (Organ) this.organDomain.query(organId);
		model.put("list", list);
		model.put("page", page);
		model.put("organ", organ);
		return new ModelAndView(getCustomPage("selectlist"), model);
	}
	/***
	 * 单选列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectsinglelist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map model = HttpUtil.convertModel(request);
		Page page = HttpUtil.convertPage(request);
		String organId = HttpUtil.getString(request, "organId",
				"11111111111111111111111111111111");
		DetachedCriteria criteria = this.baseDomain.getCriteria(null);
		criteria.add(Restrictions.eq("organId", organId));
		String name = HttpUtil.getString(request, "tname", "");
		if (!(name.equals("")))
			criteria.add(Restrictions.ilike("tname", "%" + name + "%"));
		List list = this.baseDomain.queryByCriteria(criteria, page);
		Organ organ = (Organ) this.organDomain.query(organId);
		model.put("list", list);
		model.put("page", page);
		if(organ!=null){
			model.put("organId", organ.getId());
		}
		return new ModelAndView(getCustomPage("selectsinglelist"), model);
	}
	/***
	 * 导入用户数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void importdata(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			FileUpload entity=new FileUpload();
			this.bind(request, entity);
			MultipartFile file=entity.getFile();
			int successsize=0;
			String msg="";
			int sortCode=this.usersDomain.getMaxSortCode();
			if(file!=null&&!file.isEmpty()){
				Workbook book = Workbook.getWorkbook(file.getInputStream());
				int sheetsize=book.getNumberOfSheets();
				if(sheetsize>0){
					List<AppesAttribute> listColumns=new ArrayList();
					for(int t=0;t<sheetsize;t++){
						Sheet sh = book.getSheet(t);
						int rows = sh.getRows();
						List<BaseObject> savelist=new ArrayList<BaseObject>();
						Map<String,Organ> organMap=new HashMap<String,Organ>();
						Map<String,String> hasusername=new HashMap<String,String>();
						for (int i = 1; i < rows; i++) {
							String id = sh.getCell(0, i).getContents().trim();
							String name = sh.getCell(1, i).getContents().trim();
							String sex = sh.getCell(2, i).getContents().trim();
							String duty = sh.getCell(3, i).getContents().trim();
							String organName = sh.getCell(4, i).getContents().trim();
							System.out.println("name="+name);
							System.out.println("id="+id);
							if(name==null||"".equals(name)) continue;
							//检索用户名是否可用
							Object obj=baseDomain.query(id);
							if(obj!=null){
								continue;
							}else{
								if(hasusername.containsKey(id)){
									continue;
								}else{
									hasusername.put(id, id);
								}
							}
							//检索部门
							Organ organ=null;
							if(!organMap.containsKey(organName)){
								List organlist=organDomain.queryByProperty("tname", organName);
								if(organlist.size()==1){
									organ=(Organ)organlist.get(0);
									organMap.put(organName, organ);
								}else if(organlist.isEmpty()){
									List organShotlist=organDomain.queryByProperty("shortName", organName);
									if(organShotlist.size()==1){
										organ=(Organ)organShotlist.get(0);
										organMap.put(organName, organ);
									}
								}
							}else{
								organ=organMap.get(organName);
							}
							Users user=(Users) this.baseDomain.getBaseObject();
							user.setTname(name);
							user.setOrganId(organ.getId());
							user.setId(id);
							user.setPassword("e10adc3949ba59abbe56e057f20f883e");
							user.setSortCode(sortCode);
							sortCode++;
							this.saveBaseInfoToObject(request, user);
							successsize++;
							savelist.add(user);
						}
						this.baseDomain.save(savelist);
					}
					this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
				}else{
					msg="请上传excel文件！";
					this.outFailString(request,response, msg,"");
				}
				
			}else{
				msg="请上传excel文件！";
				this.outFailString(request,response, msg,"");
			}
	}
	/**
	 * 彻底删除用户及对应的用户对象的角色功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String method=request.getMethod().toLowerCase();
		StringBuilder buffer=new StringBuilder("用户：");
		String tnames="";
		if("post".equals(method)){
			try{
				List deleteList=new ArrayList();
				//用户信息
				List<BaseObject> userslist=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.in("id", ids)));
				for(BaseObject baseObject:userslist){
					if(baseObject.isValid()){
						ids.remove(baseObject.getId());
						tnames+=baseObject.getTname()+" ";
					}
				}
				if(ids.size()>0){
					List<BaseObject> list=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.in("id", ids)));
					deleteList.addAll(list);
					//用户所属于的角色信息
					List rolelist=roleUsersDomain.queryByCriteria(roleUsersDomain.getCriteria(null).add(Restrictions.in("usersId", ids)));
					deleteList.addAll(rolelist);
					//部门管理员信息
					List organAdministratorList=baseDomain.queryByCriteria(DetachedCriteria.forClass(OrganAdministrator.class).add(Restrictions.in("usersId", ids)));
					deleteList.addAll(organAdministratorList);
					//群组信息
					List groupUserList=baseDomain.queryByCriteria(DetachedCriteria.forClass(GroupUsers.class).add(Restrictions.in("usersId", ids)));
					deleteList.addAll(groupUserList);
					baseDomain.deleteAndSaveAndUpdate(deleteList,null,null);
				}
				if(tnames.length()>0){
					buffer.append(tnames);
					buffer.append("启用中，不能进行删除！");
					this.outSuccessString(request,response,buffer.toString());
					return;
				}
			}catch(Exception e){
				String msg="数据已经被使用，不能删除!";
				this.outFailString(request,response,msg,"");
			}
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
		}
	}
	/***
	 * 用户角色编辑
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showEditRole(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> model =new HashMap<String, Object>();
		String id=HttpUtil.getString(request,"id","");
		List<RoleUsers> rolelist=roleUsersDomain.queryByProperty("usersId",id);
		DetachedCriteria criteria=roleDomain.getCriteria(true);
		List<Role> list=roleDomain.queryByCriteria(criteria);
		int count=0;
		for(Role role:list){
			for (RoleUsers roleUsers:rolelist) {
				if(role.getId().equals(roleUsers.getRoleId())){
					role.setValid(false);
					count+=1;
				}
			}
		}
		if(list.size()==count){
			model.put("checkAll", true);
		}else{
			model.put("checkAll", false);
		}
		model.put("userId", id);
		model.put("list", list);
		return new ModelAndView(getCustomPage("ShowEditRole"),model);
	}
	/***
	 * 用户角色保存
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void showSaveRole(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> roleIds=HttpUtil.getList(request,"roleIds",";");
		String userId=HttpUtil.getString(request, "userId","");
		Users users=(Users) usersDomain.query(userId);
		if(users==null){
			this.outFailString(request,response, "获取用户ID失败！","");
			return;
		}
		try{
			List deleteList=roleUsersDomain.queryByProperty("usersId",users.getId());
			List saveList=new ArrayList();
			for(String id:roleIds){
				RoleUsers roleUsers=new RoleUsers();
				roleUsers.setRoleId(id);
				roleUsers.setUsersId(users.getId());
				roleUsers.setTname(users.getTname());
				roleUsers.setValid(true);
				saveList.add(roleUsers);
			}
			this.roleUsersDomain.deleteAndSaveAndUpdate(deleteList, saveList,null);
			this.outSuccessString(request,response,"");
		}catch(Exception e){
			this.outFailString(request,response, "操作失败！","");
		}
	}
	
	public ModelAndView schoolselect(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		Users users=HttpUtil.getLoginUser(request);
		Page page = HttpUtil.convertPage(request);
		DetachedCriteria criteria = organDomain.getCriteria(true);
		String tname=HttpUtil.getString(request, "tname","");
		criteria.add(Restrictions.eq("parentId", "10000"));
		criteria.add(Restrictions.ne("parentId",users.getOrganId()));
		if(!tname.equals("")){
				criteria.add(Restrictions.ilike("tname", tname, MatchMode.ANYWHERE));
				List organs=organDomain.queryByCriteria(criteria);
				model.put("list", organs);
				model.put("page", page);
		}else {
			List organs=organDomain.queryByCriteria(criteria);
			model.put("list", organs);
			model.put("page", page);
		}
		return new ModelAndView(this.getCustomPage("schoolselect"), model);
	}
	public void setOrganDomain(OrganDomain organDomain) {
		this.organDomain = organDomain;
		this.setBaseTreeDomain(organDomain);
	}
	public void setRoleUsersDomain(RoleUsersDomain roleUsersDomain) {
		this.roleUsersDomain = roleUsersDomain;
	}
	public void setJobPostDomain(JobPostDomain jobPostDomain) {
		this.jobPostDomain = jobPostDomain;
	}
	public void setRoleDomain(RoleDomain roleDomain) {
		this.roleDomain = roleDomain;
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
}
