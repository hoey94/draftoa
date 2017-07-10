package com.starsoft.core.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Resource;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class ResourceController extends BaseAjaxController implements BaseInterface {
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
		Map<String,Object> model = new HashMap<String,Object>();
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
	/**
	 * 彻底删除对象功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				List list=this.baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.in("id", ids)));
				for(int t=0;t<list.size();t++){
					Resource object=(Resource)list.get(t);
					String url=object.getUrl().replace("/", "\\").substring(1);
					String filepath=getServletContext().getRealPath("/")+url;
					File file=new File(filepath);
					if(file.exists()){
						file.delete();
					}
				}
				baseDomain.deletes(ids);
				this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
			}catch(Exception e){
				String msg="数据已经被使用，不能删除!";
				this.outFailString(request,response,msg,"");
			}
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
		}
	}
}
