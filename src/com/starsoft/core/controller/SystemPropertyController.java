package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.SystemProperty;
import com.starsoft.core.entity.SystemPropertyPerson;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;

public class SystemPropertyController extends BaseAjaxController implements BaseInterface {
	private String sysParam;
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
		String gotourl=request.getRequestURI();
		String method=request.getMethod().toLowerCase();
		gotourl=request.getRequestURI();
		if("post".equals(method)){
			SystemProperty entity=(SystemProperty)baseDomain.getBaseObject();
			try{
				this.bind(request, entity);
				this.saveBaseInfoToObject(request, entity);
				List list = baseDomain.queryByProperty("tname", entity.getTname());
				if(list.size()>0){
					this.outFailString(request,response, "系统已经存在该属性了，属性名称："+entity.getTname(),gotourl);
				}
				entity.setValid(false);
				this.baseDomain.save(entity);
				WEBCONSTANTS.setSystemProperty(entity.getId(), entity.getTname());
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
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		String method=request.getMethod().toLowerCase();
		String gotourl=request.getRequestURI()+"?page="+page;
		if("post".equals(method)){
			try{
				SystemProperty entity=(SystemProperty) baseDomain.query(id);
				boolean oldvalid=entity.isValid();
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				if(oldvalid&&!entity.isValid()){//禁止用户自定义操作
					String hql = "delete from "+SystemPropertyPerson.class.getName()+" where systemPropertyId = ?";
					List parameters=new ArrayList();
					parameters.add(id);
					baseDomain.executeByHQL(hql, parameters);
				}
				WEBCONSTANTS.setSystemProperty(entity.getId(), entity.getTname());
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
	 * 彻底删除对象功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			String result="";
			try{
				List<SystemProperty> list=this.baseDomain.queryByProperty("id", ids);
				for(SystemProperty systemProperty:list){
					if(!systemProperty.isValid()){//系统核心参数不能删除
						result+=systemProperty.getTname()+";";
					}else{
						baseDomain.delete(systemProperty);
					}
				}
			}catch(Exception e){
				String msg="数据已经被使用，不能删除!";
				this.outFailString(request,response,msg,"");
			}
			if(!result.equals("")){
				this.outSuccessString(request,response, result);
			}else{
				this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
			}
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
		}
	}
	public String getSysParam() {
		return sysParam;
	}
	public void setSysParam(String sysParam) {
		this.sysParam = sysParam;
		List<String> list=StringUtil.toList(sysParam, "#");
		for(String str:list){
			if(str.indexOf("=")>-1){
				String[] params=str.split("=");
				String key=params[0];
				String value=params[1];
				WEBCONSTANTS.setSystemProperty(key, value);
			}
		}
	}
}
