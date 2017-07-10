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

import com.starsoft.core.domain.SystemPropertyDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.SystemProperty;
import com.starsoft.core.entity.SystemPropertyPerson;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class SystemPropertyPersonController extends BaseAjaxController implements BaseInterface {
	private SystemPropertyDomain systemPropertyDomain;
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
		//先初始化个性化配置信息
		List<SystemProperty> systemList=systemPropertyDomain.queryByCriteria(systemPropertyDomain.getCriteria(true));
		Users users=HttpUtil.getLoginUser(request);
		List saveList=new ArrayList();
		for(SystemProperty systemProperty:systemList){
			String id=systemProperty.getId();
			String createId=users.getId();
			List personlist=this.baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("systemPropertyId", id)).add(Restrictions.eq("createId", createId)));
			if(personlist.size()<1){
				SystemPropertyPerson systemPropertyPerson=new SystemPropertyPerson();
				systemPropertyPerson.setCreateId(createId);
				systemPropertyPerson.setDescription(systemProperty.getDescription());
				systemPropertyPerson.setSystemPropertyId(id);
				systemPropertyPerson.setTname(systemProperty.getTname());
				systemPropertyPerson.setValid(true);
				saveList.add(systemPropertyPerson);
			}
		}
		this.baseDomain.save(saveList);
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
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
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.edit(request, response);
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
		if("post".equals(method)){
			try{
				SystemPropertyPerson entity=(SystemPropertyPerson)baseDomain.getBaseObject();
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
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				SystemPropertyPerson entity=(SystemPropertyPerson) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				WebUtils.setSessionAttribute(request,entity.getSystemPropertyId(), entity.getTname());
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	public void setSystemPropertyDomain(SystemPropertyDomain systemPropertyDomain) {
		this.systemPropertyDomain = systemPropertyDomain;
	}
}
