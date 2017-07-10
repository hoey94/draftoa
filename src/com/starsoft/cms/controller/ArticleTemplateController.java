package com.starsoft.cms.controller;

import java.io.File;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.cms.entity.ArticleTemplate;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.FileUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class ArticleTemplateController extends BaseAjaxController  {
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
		ArticleTemplate obj=(ArticleTemplate) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			model.put("page", page);
			Clob content=obj.getTemplateContent();
			try {
				if(content!=null&&content.length()>1){
					String contents=content.getSubString((long)1,(int)content.length());
					model.put("content", contents);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch blockfolderPath
				e.printStackTrace();
			}
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
		ModelAndView model=this.edit(request, response);
		return new ModelAndView(this.getReadPage(),model.getModel());
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
		String content=HttpUtil.getString(request, "content", "");
		String projectPath=getServletContext().getRealPath("/");
		String folderPath=projectPath+"page\\cms\\articleTemplate\\";
		if("post".equals(method)){
			try{
				ArticleTemplate entity=(ArticleTemplate)baseDomain.getBaseObject();
				this.bind(request, entity);
				this.saveBaseInfoToObject(request, entity);
				Clob contents = Hibernate.createClob(content);
				entity.setTemplateContent(contents);
				this.baseDomain.save(entity);
				FileUtil.WriteFile(folderPath+entity.getId()+".htm", content);
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
		String content=HttpUtil.getString(request, "content", "");
		String projectPath=getServletContext().getRealPath("/");
		String folderPath=projectPath+"page\\cms\\articleTemplate\\";
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				ArticleTemplate entity=(ArticleTemplate) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				Clob contents = Hibernate.createClob(content);
				entity.setTemplateContent(contents);
				this.baseDomain.update(entity);
				File file = new File(folderPath+entity.getId()+".htm");
				file.delete();
				FileUtil.WriteFile(folderPath+entity.getId()+".htm", content);
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
		String projectPath=getServletContext().getRealPath("/");
		String folderPath=projectPath+"page\\cms\\articleTemplate\\";
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				baseDomain.deletes(ids);
				for(String str:ids){
					File file = new File(folderPath+str+".htm");
					file.delete();
				}
				this.outSuccessString(request,response,"");
			}catch(Exception e){
				String msg="数据已经被使用，不能删除!";
				this.outFailString(request,response,msg,"");
			}
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
		}
	}
}
