package com.starsoft.cms.controller;

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

import com.starsoft.cms.domain.ImagePlayDomain;
import com.starsoft.cms.domain.ImagePlayRelationImagesDomain;
import com.starsoft.cms.entity.ImagePlay;
import com.starsoft.cms.entity.ImagePlayRelationImages;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.domain.ResourceDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Resource;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class ImagePlayRelationImagesController extends BaseAjaxController implements BaseInterface {
	private ResourceDomain resourceDomain;
	private ImagePlayDomain imagePlayDomain;
	private ImagePlayRelationImagesDomain imagePlayRelationImagesDomain;
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
		String imagePlayId=HttpUtil.getString(request, "imagePlayId", "");
		DetachedCriteria criteria = this.convertCriteria(request);
		if(!imagePlayId.equals("")){
			criteria.add(Restrictions.eq("imagePlayId", imagePlayId));
		}
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		model.put("imagePlayId", imagePlayId);
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
		String imagePlayId=HttpUtil.getString(request, "imagePlayId", "");
		model.put("imagePlayId", imagePlayId);
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
		String imagePlayId=HttpUtil.getString(request, "imagePlayId", "");
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject obj=(BaseObject) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			model.put("page", page);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		model.put("imagePlayId", imagePlayId);
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
		String imagePlayId=HttpUtil.getString(request, "imagePlayId", "");
		String gotourl=request.getRequestURI()+"?imagePlayId="+imagePlayId;
		if("post".equals(method)){
			try{
				ImagePlayRelationImages entity=(ImagePlayRelationImages)baseDomain.getBaseObject();
				this.bind(request, entity);
				this.saveBaseInfoToObject(request, entity);
				entity.setSortCode(imagePlayRelationImagesDomain.getMaxSortCode());
				this.baseDomain.save(entity);
				WEBCONSTANTS.articleupdatestate=true;
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
		String imagePlayId=HttpUtil.getString(request, "imagePlayId", "");
		String gotourl=request.getRequestURI()+"?page="+page+"&imagePlayId="+imagePlayId;
		if("post".equals(method)){
			try{
				BaseObject entity=(BaseObject) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				WEBCONSTANTS.articleupdatestate=true;
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
	public ModelAndView selectImages(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		Page page=HttpUtil.convertPage(request);
		String imagePlayId=HttpUtil.getString(request, "imagePlayId", "");
		DetachedCriteria criteria=resourceDomain.getCriteria(true);
		String name=HttpUtil.getString(request, "tname","");
		if(!name.equals("")){
			criteria.add(Restrictions.ilike("tname", "%"+name+"%"));
		}
		criteria.addOrder(Order.desc("sortCode"));
		criteria.add(Restrictions.eq("fileType", "image"));
		List list= resourceDomain.queryByCriteria(criteria, page);
		if(!page.getHasNext()){
			int size=list.size();
			int supplysize=5-size%5;
			if(supplysize<5){
				for(int t=0;t<supplysize;t++){
					list.add(new Object());
				}
			}
		}
		model.put("list", list);
		model.put("page", page);
		model.put("imagePlayId", imagePlayId);
		return new ModelAndView(this.getCustomPage("selectImages"),model);
	}
	/***
	 * 批量保存
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void savemore(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String method=request.getMethod().toLowerCase();
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String imagePlayId=HttpUtil.getString(request, "imagePlayId", "");
		if("post".equals(method)&&ids.size()>0){
			List saveList=new ArrayList();
			try{
				for(String id:ids){
					List list=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("imagePlayId", imagePlayId)).add(Restrictions.eq("resourceId", id)));
					if(list.size()<1){
						Resource resource=(Resource)this.resourceDomain.query(id);
						ImagePlayRelationImages entity=(ImagePlayRelationImages) baseDomain.getBaseObject();
						entity.setImagePlayId(imagePlayId);
						entity.setResourceId(id);
						this.saveBaseInfoToObject(request, entity);
						if(resource!=null){
							entity.setImageurl(resource.getUrl());
							entity.setTname(resource.getTname());
							entity.setLinktext(resource.getTname());
							saveList.add(entity);
						}
					}
				}
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, "系统出现异常！","");
			}
			this.baseDomain.save(saveList);
		}else{
			this.outFailString(request,response, "您没有操作权限！","");
		}
	}
	/***
	 * 预览效果
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView preview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		Map<String,Object> model = new HashMap<String,Object>();
		ImagePlay imagePlay=(ImagePlay)this.imagePlayDomain.query(id);
		if(imagePlay!=null){
			Page page=new Page(0,6);
			if(imagePlay.getMaxImageNum()!=null){
				page=new Page(0,imagePlay.getMaxImageNum());
			}
			List imageList=this.baseDomain.queryByCriteria(this.baseDomain.getCriteria(true).add(Restrictions.eq("imagePlayId", id)), page);
			model.put("imagePlay", imagePlay);
			model.put("imageList", imageList);
		}
		return new ModelAndView(this.getCustomPage("preview"),model);
	}
	public void setResourceDomain(ResourceDomain resourceDomain) {
		this.resourceDomain = resourceDomain;
	}
	public void setImagePlayDomain(ImagePlayDomain imagePlayDomain) {
		this.imagePlayDomain = imagePlayDomain;
	}
	public void setImagePlayRelationImagesDomain(
			ImagePlayRelationImagesDomain imagePlayRelationImagesDomain) {
		this.imagePlayRelationImagesDomain = imagePlayRelationImagesDomain;
	}
}
