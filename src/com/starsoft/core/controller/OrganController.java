package com.starsoft.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.OrganAdministrator;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.Pinyin4jUtil;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;

public class OrganController extends BaseTreeController implements BaseInterface {
	private UsersDomain usersDomain;
	private OrganDomain organDomain;
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
			model.put("deleteAndAdd", "true");// 添加节点和删除节点的权利
			model.put("rightContent", "?action=list&sortfield=sortCode&sortvalue=false");
			model.put("urlLink", "?action=read&id=");
			model.put("addLink", "organ.do?action=add&parentId=");
			return new ModelAndView("baseframe/subframetree",model);
		}else{
			String administratorOrganId=organDomain.queryTopAdministratorOrgan(user.getId());
			if(!administratorOrganId.equals("")){
				if(!administratorOrganId.equals(parentId)){
					parentId=administratorOrganId;
				}
				
				if(!parentId.equals(rootValue)){
					BaseTreeObject topNode=(BaseTreeObject) baseDomain.query(parentId);
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
				model.put("deleteAndAdd", "true");// 添加节点和删除节点的权利
				model.put("rightContent", "?action=list&sortfield=sortCode&sortvalue=false");
				model.put("urlLink", "?action=read&id=");
				model.put("addLink", "organ.do?action=add&parentId=");
				return new ModelAndView("baseframe/subframetree",model);
			}else{
				model.put("list", list);
				model.put("title", "组织部门");
				model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
				model.put("rightContent", "?action=list&sortfield=sortCode&sortvalue=false");
				model.put("urlLink", "?action=read&id=");
				model.put("addLink", "organ.do?action=add&parentId=");
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
		criteria.add(Restrictions.ne("id", rootValue));
		Users user=HttpUtil.getLoginUser(request);
		String parentId="";
		if(user!=null){
			user.getOrganId();
		}
		if(parentId.equals(rootValue)||"admin".equals(user.getId())){//根部门下的用户或者超级管理员
//			List list= baseDomain.queryByCriteria(criteria, page);
			List list= organDomain.queryTreeByParentId(rootValue,2,null,false);
			model.put("list", list);
		}else{
			String administratorOrganId=organDomain.queryTopAdministratorOrgan(user.getId());
			List list=new ArrayList();
			if(!administratorOrganId.equals("")){
				parentId=administratorOrganId;
				list= organDomain.queryByCriteria(criteria.add(Restrictions.eq("parentId", parentId)), page);
			}
			model.put("list", list);
		}
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
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Map<String,Object> model = HttpUtil.convertModel(request);
		DetachedCriteria criteria = organDomain.getCriteria(true,false);
		criteria.add(Restrictions.ne("id", id));
		Organ obj=(Organ) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			if(obj.getParentId()!=null&&!"".equals(obj.getParentId())){
				Organ parentObj=(Organ) baseDomain.query(obj.getParentId());
				if(parentObj!=null){
					model.put("parentName", parentObj.getTname());
				}
			}
			model.put("page", page);
			List organAdmins=organDomain.queryOrganAdministrator(id);
			model.put("organAdmins", organAdmins);
			DetachedCriteria usercriteria = DetachedCriteria.forClass(Users.class);
			String organId = HttpUtil.getString(request, "id","");
			usercriteria.add(Restrictions.eq("valid", true));
			usercriteria.add(Restrictions.eq("organId", organId));
			usercriteria.add(Restrictions.ne("id", "admin"));
			usercriteria.addOrder(Order.asc("sortCode"));
			List list= baseDomain.queryByCriteria(usercriteria);
			model.put("list", list);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		return new ModelAndView(this.getReadPage(),model);
	}
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addressbook(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("rightContent", "users.do?action=readlist&sortfield=sortCode&sortvalue=true");
		model.put("leftTree", "?action=addressbooktree");
		return new ModelAndView("baseframe/subframes",model);
	}
	/***
	 * 企业通讯录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addressbooktree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		List list=new ArrayList();
		if(!parentId.equals(rootValue)){
			BaseTreeObject topNode=(BaseTreeObject) baseDomain.query(parentId);
			if(topNode!=null){
				topNode.setParentId(rootValue);
				list.add(topNode);
			}
		}
		List lists= organDomain.queryTreeByParentId(parentId,5,null,false);
		list.addAll(lists);
		model.put("list", list);
		model.put("title", "组织部门");
		model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
		model.put("rightContent", "users.do?action=readlist&sortfield=sortCode&sortvalue=true");
		model.put("urlLink", "users.do?action=readlist&sortfield=sortCode&sortvalue=true&organId=");
		model.put("addLink", "organ.do?action=add&parentId=");
		return new ModelAndView("baseframe/subframetree",model);
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
		Map<String,Object> model = HttpUtil.convertModel(request);
		DetachedCriteria criteria = organDomain.getCriteria(true,false);
		criteria.add(Restrictions.ne("id", id));
		Organ obj=(Organ) baseDomain.query(id);
		List list=new ArrayList();
		if(obj!=null){
			boolean caneditparentId=true;
			String parentId=obj.getParentId();
			Users user=HttpUtil.getLoginUser(request);
			if("admin".equals(user.getId())){//超级管理员管理所有的部门信息
				List lists= organDomain.queryTreeByBaseObjectIdNotContainId(user.getOrganId(),id,4,null,false);
				list.addAll(lists);
			}else{//非超级管理员
				String administratorOrganId=organDomain.queryTopAdministratorOrgan(user.getId());
				if(id.equals(administratorOrganId)){//所属上级不可以编辑
					caneditparentId=false;
					Organ parentOrgan=(Organ)organDomain.query(obj.getParentId());
					if(parentOrgan!=null){
						model.put("parentOrganName", parentOrgan.getTname());
					}
				}else if(!"".equals(administratorOrganId)){//所属上级可以编辑
					list=organDomain.queryTreeByBaseObjectIdNotContainId(administratorOrganId, id, 4, null, false);
				}else{
					model.put("msg", "当前用户不存在可以管理的部门信息!");
					return new ModelAndView(CloseMessagePage,model);
				}
			}
			model.put("parentlist", list);
			model.put("obj", obj);
			model.put("caneditparentId", caneditparentId);
			List userslist=usersDomain.queryByCriteria(usersDomain.getCriteria(true).add(Restrictions.eq("organId", id)));
			model.put("userslist", userslist);
			model.put("page", page);
			List organAdmins=organDomain.queryOrganAdministrator(id);
			model.put("organAdmins", organAdmins);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		return new ModelAndView(this.getEditPage(),model);
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
		Map<String,Object> model = HttpUtil.convertModel(request);
		String parentId=HttpUtil.getString(request, "parentId", rootValue);
		if(!model.containsKey("parentId")||model.get("parentId")==null||"".equals(model.get("parentId"))){
			model.put("parentId", parentId);
		}
		Users user=HttpUtil.getLoginUser(request);
		String administratorOrganId=organDomain.queryTopAdministratorOrgan(user.getId());
		List list=new ArrayList();
		if(!administratorOrganId.equals("")){
			list.add(organDomain.queryTreeByBaseObjectId(administratorOrganId,4,true, false));
		}
		model.put("parentlist", list);
		return new ModelAndView(this.getAddPage(),model);
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
			Users users=HttpUtil.getLoginUser(request);
			boolean canEditOrgon = organDomain.canEditOrgan(id, users.getId());
			if("admin".equals(users.getId())){
				canEditOrgon=true;
			}
			if(!canEditOrgon){
				this.outFailString(request,response,"您不具备修改部门权利",gotourl);
			}else{
				Organ entity=(Organ) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				List userIds=HttpUtil.getList(request, "userIds", ";");
				organDomain.updateOrganAdministrator(id, userIds);
				this.outSuccessString(request,response, gotourl);
			}
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
	public void save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gotourl="";
		Organ entity=(Organ)baseDomain.getBaseObject();
		gotourl=request.getRequestURI()+"?sortfield=sortCode&sortvalue=true";
		try{
			this.bind(request, entity);
			this.saveBaseInfoToObject(request, entity);
			entity.setParentId(rootValue);
			this.baseDomain.save(entity);
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/***
	 *选择部门
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectOrgans(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		String parentId = HttpUtil.getString(request, "parentId",rootValue);
		List list = this.organDomain.queryTreeByParentId(parentId,5,Boolean.valueOf(true), Boolean.valueOf(false));
		String selectIds = HttpUtil.getString(request,"selectOrganIds", "");
		String selectType = HttpUtil.getString(request,"selectType", "0");
		model.put("selectIds", selectIds);
		model.put("organlist", list);
		if("0".equals(selectType)){//多选
			model.put("selectType", true);
		}else{
			model.put("selectType", false);
		}
		return new ModelAndView(getCustomPage("select"), model);
	}
	/**
	 * 彻底删除对象功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String method=request.getMethod().toLowerCase();
		String result="";
		if("post".equals(method)){
			try{
				List<Organ> list=baseDomain.queryByProperty("id", ids);
				Users users=HttpUtil.getLoginUser(request);
				for(Organ organ:list){
					if(rootValue.equals(organ.getParentId())){
						result+="顶级组织("+organ.getTname()+")不能删除，请联系管理员\n";
						organ.setValid(false);
						organDomain.update(organ);
						this.outSuccessString(request, response, "");
					}else{
						boolean canEditOrgon = organDomain.canEditOrgan(organ.getId(), users.getId());
						if(canEditOrgon){
							String parentId=organ.getParentId();
							//删除部门管理员信息
							List deleteList=organDomain.queryByCriteria(DetachedCriteria.forClass(OrganAdministrator.class).add(Restrictions.eq("organId", organ.getId())));
							baseDomain.deleteAndSaveAndUpdate(deleteList, null, null);
							organDomain.delete(organ);
							//部门下的人员修改为上级部门人员
//							String sqlupdateuser="update T_CORE_USER set organId="+parentId+" where organId="+organ.getId();
//							organDomain.executeBySQL(sqlupdateuser, null);
						}else{
							result+="\n组织("+organ.getTname()+")不能删除，您不具备删除的权利";
						}
					}
				}
				this.outSuccessString(request, response, "");
			}catch(Exception e){
				String msg="数据已经被使用，不能直接删除!，请先删除用户，在删除组织信息";
				this.outFailString(request,response,msg,"");
			}
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
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
			String pingyin=Pinyin4jUtil.getPinYinHeadChar(tname);
			Object obj=this.baseDomain.queryFirstByProperty("organCode", pingyin);
			if(obj!=null){
				for(int t=1;t<20;t++){
					obj=this.baseDomain.queryFirstByProperty("organCode", pingyin+t);
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
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
	public void setOrganDomain(OrganDomain organDomain) {
		this.organDomain = organDomain;
		this.setBaseTreeDomain(organDomain);
	}
	
	////导出机构
	/**
	 * 导出所有数据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Autowired
	private AppesDomain appesDomain;

	public void exportall(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Users users = HttpUtil.getLoginUser(request);
		if (users == null) {
			this.outFailString(request, response, "您没有登陆，无权导出", "");
			return;
		}
		boolean hasdata = false;// 是否有数据;
		Appes appe = (Appes) this.appesDomain.query("organ");
		if (appe == null) {
			this.outFailString(request, response, "系统不存在要导出的数据！", "");
			return;
		}
		List datalist = this.baseDomain.queryByCriteria(DetachedCriteria
				.forClass(Class.forName(appe.getClassName()))
				.add(Restrictions.eq("valid", true)).add(Restrictions.eq("createId", users.getId())));
		String templatePath = getServletContext().getRealPath("/masterplate/student1.xls");
		File file = new File(templatePath);
		if(!file.exists()){
			this.outFailString(request, response, "系统模板不存在！", "");
			return;
		}
		FileInputStream inputStream = new FileInputStream(file);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		hasdata = true;
		/***初始化模板***/
        List<String> columnnameEn=new ArrayList<String>();
        List<String> columnnameCh=new ArrayList<String>();
        hasdata=true;
        HSSFSheet sh = workbook.getSheetAt(0);
        HSSFRow row = sh.getRow(2);
        for(int t = 0; t < 5; t++){
        	String colName = row.getCell(t).getStringCellValue().trim();
        	if(colName.equals("序号")){
        		columnnameEn.add("");
        	}else if (colName.equals("机构名称")) {
        		columnnameEn.add("tname");
			}else if (colName.equals("机构类型")) {
				columnnameEn.add("organType");
			}else if (colName.equals("机构领导")) {
				columnnameEn.add("leaderName");
			}else if (colName.equals("机构状态")) {
				columnnameEn.add("valid");
			}
        	columnnameCh.add(colName);
        }
		for (int t = 0; t < datalist.size(); t++) {
			HSSFRow sheetHeadRowTemp = null;
			sheetHeadRowTemp = sh.createRow(t + 3);
			BaseObject obj = (BaseObject) datalist.get(t);
			for (int s = 0; s < 5; s++) {
				String value = "";
				String fieldname = columnnameEn.get(s);
				String fieldnameCh = columnnameCh.get(s);
				if(fieldnameCh.equals("序号")){
					value = t + 1 + "";
				}else {
					if(!StringUtil.isNullOrEmpty(fieldname)){
						value = BeanUtils.getProperty(obj, fieldname);
					}else {
						value = "";
					}
				}
				sheetHeadRowTemp.createCell(s).setCellValue(value);
			}
		}
		if (hasdata) {// 说明有待导出的数据
			String filename = "data1.xls";// 设置下载时客户端Excel的名称
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = response.getOutputStream();
			workbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} else {
			this.outFailString(request, response, "系统不存在要导出的数据！", "");
		}

	}


}
