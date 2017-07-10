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

import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.AppesAttribute;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class AppesAttributeController extends BaseAjaxController implements BaseInterface {
	AppesDomain appesDomain;
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
		DetachedCriteria criteria = baseDomain.getCriteria(null);
		String name=HttpUtil.getString(request, "tname","");
		if(!name.equals("")){
			criteria.add(Restrictions.ilike("tname", "%"+name+"%"));
		}
		String app=HttpUtil.getString(request, "app","");
		Appes appes=(Appes)appesDomain.query(app);
		List list=new ArrayList();
		if(appes!=null){
			criteria.add(Restrictions.eq("appes", app));
			criteria.addOrder(Order.asc("id"));
			list= baseDomain.queryByCriteria(criteria, page);
			model.put("list", list);
		}
		model.put("page", page);
		model.put("appes", appes);
		model.put("app", app);
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
		Map<String,Object> model = new HashMap<String,Object>();
		String gotourl="";
		String method=request.getMethod().toLowerCase();
		String app="";
		if("post".equals(method)){
			AppesAttribute entity=(AppesAttribute)baseDomain.getBaseObject();
			try{
				this.bind(request, entity);
				this.saveBaseInfoToObject(request, entity);
				this.baseDomain.save(entity);
				app=entity.getAppes();
				gotourl=request.getRequestURI()+"?page="+0+"&app="+app;
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
		Map<String,Object> model = new HashMap<String,Object>();
		String gotourl=HttpUtil.getString(request, "gotourl", "?action=list");
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		String method=request.getMethod().toLowerCase();
		String app="";
		if("post".equals(method)){
			try{
				AppesAttribute entity=(AppesAttribute) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				app=entity.getAppes();
				gotourl=request.getRequestURI()+"?page="+page+"&app="+app;
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
	 * 更新状态信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void updateBaseObject(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
			String method=request.getMethod().toLowerCase();
			String id=HttpUtil.getString(request, "id", "");
			String name=HttpUtil.getString(request, "tname", "");
			String value=HttpUtil.getString(request, "value", "");
			if(!id.equals("")&&"post".equals(method)){	
				AppesAttribute obj=(AppesAttribute) baseDomain.query(id);
				if(obj!=null){
					if("sortCode".equals(name)){
						obj.setSortCode(Integer.valueOf(value));
					}else if("tname".equals(name)){
						obj.setTname(value);
					}else if("valid".equals(name)){
						obj.setValid("true".equals(value));
					}else if("description".equals(name)){
						obj.setTname(value);
					}else if("addDisplay".equals(name)){
						obj.setAddDisplay("true".equals(value));
					}else if("editDisplay".equals(name)){
						obj.setEditDisplay("true".equals(value));
					}else if("listDisplay".equals(name)){
						obj.setListDisplay("true".equals(value));
					}else if("nullValue".equals(name)){
						obj.setNullValue("true".equals(value));
					}
					baseDomain.update(obj);
					this.outSuccessString(request,response,"");
				}
			}else{			
				this.outFailString(request,response, "更新失败！","");
			}
	}
	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}
}
