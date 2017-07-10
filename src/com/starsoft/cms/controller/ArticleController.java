package com.starsoft.cms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.cms.domain.ColumnsDomain;
import com.starsoft.cms.domain.ImagePlayDomain;
import com.starsoft.cms.domain.ImagePlayRelationImagesDomain;
import com.starsoft.cms.entity.Article;
import com.starsoft.cms.entity.Columns;
import com.starsoft.cms.entity.ImagePlay;
import com.starsoft.cms.entity.ImagePlayRelationImages;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.controller.BaseTreeController;
import com.starsoft.core.domain.ClobInfoDomain;
import com.starsoft.core.domain.ResourceDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class ArticleController extends BaseTreeController implements BaseInterface {
	private ColumnsDomain columnsDomain;
	private ClobInfoDomain clobInfoDomain;
	private ImagePlayDomain imagePlayDomain;
	private ImagePlayRelationImagesDomain imagePlayRelationImagesDomain;
	private ResourceDomain resourceDomain;
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
		List list= columnsDomain.queryTreeByParentId(this.rootValue,4,null,false);
		model.put("list", list);
		model.put("title", "栏目");
		model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
		model.put("rightContent", "?action=list&sortfield=sortCode&sortvalue=false");
		model.put("urlLink", "?action=list&columnId=");
		model.put("addLink", "organ.do?action=add&parentId=");
		return new ModelAndView("baseframe/subframetree",model);
	}
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("title", "站点栏目管理");
		return new ModelAndView("baseframe/lefttree",model);
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
		String columnId=HttpUtil.getString(request, "columnId", rootValue);
		Page page=HttpUtil.convertPage(request);
		Columns columns=new Columns();
		if(rootValue.equals(columnId)){
			List list= columnsDomain.queryTreeByParentId(this.rootValue,4,null,false);
			columns=(Columns)list.get(0);
			columnId=columns.getId();
		}else{
			columns= (Columns)columnsDomain.query(columnId);
		}
		// 执行测试
		DetachedCriteria criteria=baseDomain.getCriteria(null);
		String name=HttpUtil.getString(request, "tname","");
		if(!name.equals("")){
			criteria.add(Restrictions.ilike("tname", "%"+name+"%"));
		}
		criteria.addOrder(Order.desc("publishTime"));
		List list=new ArrayList();
		boolean auditState=false;
		boolean audit=false;
		if(columns==null){
			if(!rootValue.equals(columnId)){
				model.put("msg", "编辑的信息所属栏目不存在!可能已经被删除!");
				return new ModelAndView(CloseMessagePage,model);
			}
		}else{
			Columns columnTemp=(Columns)columns;
			auditState=columnTemp.isAuditState();
			criteria.add(Restrictions.eq("columnId", columnId));
			list= baseDomain.queryByCriteria(criteria, page);
			if(auditState){//如果启用审核
				String role=columnTemp.getAuditRole();
				Users users=HttpUtil.getLoginUser(request);
				List<String> roles=users.getRoles();
				for(String str:roles){
					if(str.equals(role)){
						audit=true;
						break;
					}
				}
			}
		}
		model.put("auditState", auditState);
		model.put("audit", audit);
		model.put("list", list);
		model.put("page", page);
		model.put("columnId", columnId);
		model.put("columns", columns);
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
	 * 公共的信息审核功能
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView auditinfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Map<String,Object> model = new HashMap<String,Object>();
		Article obj=(Article) baseDomain.query(id);
		boolean auditState=false;
		boolean audit=false;
		if(obj!=null){
			model.put("obj", obj);
			Columns columns=(Columns) columnsDomain.query(obj.getColumnId());
			if(columns==null){
				model.put("msg", "编辑的信息所属栏目不存在!可能已经被删除!");
				return new ModelAndView(CloseMessagePage,model);
			}
			auditState=columns.isAuditState();
			audit=false;
			String role=columns.getAuditRole();
			Users users=HttpUtil.getLoginUser(request);
			List<String> roles=users.getRoles();
			for(String str:roles){
				if(str.equals(role)){
					audit=true;
					break;
				}
			}
			model.put("columns", columns);
			model.put("page", page);
			String infoContent=clobInfoDomain.query(id);
			model.put("articleContent",infoContent);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
		}
		if(auditState){
			if(audit){
				return new ModelAndView(this.getCustomPage("auditinfo"),model);
			}else{
				model.put("msg", "您没有审核该信息的权限!");
				return new ModelAndView(this.getReadPage(),model);
			}
		}else{
			model.put("msg", "该栏目不需要审核!");
			return new ModelAndView(CloseMessagePage,model);
		}
		
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
		String columnId=HttpUtil.getString(request, "columnId", rootValue);
		Columns columns=(Columns) columnsDomain.query(columnId);
		if(columns==null){
			columns=new Columns();
		}
		model.put("columns", columns);
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
		Article obj=(Article) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			Columns columns=(Columns) columnsDomain.query(obj.getColumnId());
			if(columns==null){
				model.put("msg", "编辑的信息所属栏目不存在!可能已经被删除!");
				return new ModelAndView(CloseMessagePage,model);
			}
			model.put("columns", columns);
			model.put("page", page);
			String infoContent=clobInfoDomain.query(id);
			model.put("articleContent",infoContent);
			return new ModelAndView(this.getEditPage(),model);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
	}
	/***
	 * 信息预览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView preview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("page", page);
		Article obj=(Article) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			Columns columns=(Columns) columnsDomain.query(obj.getColumnId());
			if(columns==null){
				model.put("msg", "编辑的信息所属栏目不存在!可能已经被删除!");
				return new ModelAndView(CloseMessagePage,model);
			}
			model.put("user", HttpUtil.getLoginUser(request));
			model.put("columns", columns);
			String infoContent=clobInfoDomain.query(id);
			model.put("articleContent",infoContent);				
			return new ModelAndView(this.getReadPage(),model);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
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
		Article entity=(Article)baseDomain.getBaseObject();
		String gotourl=request.getRequestURI()+"?columnId="+entity.getColumnId();
		try{
			this.bind(request, entity);
			List saveList=new ArrayList();
			this.saveBaseInfoToObject(request, entity);
			entity.setHits(0);
			String imageurl="";
			String linkurl="web.do?action=articledetail&id="+entity.getId();
			String content=HttpUtil.getString(request, "articleContent", "");
			if(!content.equals("")){
				clobInfoDomain.save(entity.getId(), content);
			}
			entity.setStaticUrl("");
			if(entity.getImgUrl()!=null&&!entity.getImgUrl().equals("")){
				imageurl=entity.getImgUrl();
			}
			Columns column=(Columns)columnsDomain.query(entity.getColumnId());
			if(!column.isAuditState()){
				entity.setAuditTime(new Date());
				entity.setPublishTime(new Date());
				entity.setAuditer(entity.getCreateId());
				entity.setAuditState(true);
			}
			if(entity.isValid()){
				entity.setPublishTime(new Date());
			}
			entity.setSortCode(columnsDomain.getMaxArticleSortCodeByColumnId(entity.getColumnId()));
			saveList.add(entity);
			if(!imageurl.equals("")){//对图片是否进行图片展示进行处理
				List imagePlayList=imagePlayDomain.queryByCriteria(imagePlayDomain.getCriteria(true).add(Restrictions.eq("imageSource", "columnImage")).add(Restrictions.eq("columnId", entity.getColumnId())));
				String imagename="";
				BaseObject obj=resourceDomain.queryFirstByProperty("url", imageurl);
				if(obj!=null){
					imagename=obj.getTname();
				}
				for(int t=0;t<imagePlayList.size();t++){
					ImagePlay imagePlay=(ImagePlay)imagePlayList.get(t);
					ImagePlayRelationImages imagePlayRelationImages=(ImagePlayRelationImages)imagePlayRelationImagesDomain.getBaseObject();
					imagePlayRelationImages.setImagePlayId(imagePlay.getId());
					imagePlayRelationImages.setImageurl(imageurl);
					imagePlayRelationImages.setLinktext(entity.getTname());
					imagePlayRelationImages.setTname(imagename);
					imagePlayRelationImages.setLinkurl(linkurl);
					imagePlayRelationImages.setValid(true);
					this.saveBaseInfoToObject(request, imagePlayRelationImages);
					saveList.add(imagePlayRelationImages);
				}
			}
			this.baseDomain.save(saveList);
			WEBCONSTANTS.articleupdatestate=true;
			this.outSuccessString(request,response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	
	/***
	 * 信息预览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void saveform(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Article entity=(Article)baseDomain.getBaseObject();
		String gotourl=request.getRequestURI()+"?columnId="+entity.getColumnId();
		try{
			this.bind(request, entity);
			List saveList=new ArrayList();
			this.saveBaseInfoToObject(request, entity);
			entity.setHits(0);
			String imageurl="";
			String linkurl="web.do?action=articledetail&id="+entity.getId();
			String content=HttpUtil.getString(request, "articleContent", "");
			if(!content.equals("")){
				clobInfoDomain.save(entity.getId(), content);
			}
			entity.setStaticUrl("");
			if(entity.getImgUrl()!=null&&!entity.getImgUrl().equals("")){
				imageurl=entity.getImgUrl();
			}
			Columns column=(Columns)columnsDomain.query(entity.getColumnId());
			if(!column.isAuditState()){
				entity.setAuditTime(new Date());
				entity.setPublishTime(new Date());
				entity.setAuditer(entity.getCreateId());
				entity.setAuditState(true);
			}
			if(entity.isValid()){
				entity.setPublishTime(new Date());
			}
			entity.setSortCode(columnsDomain.getMaxArticleSortCodeByColumnId(entity.getColumnId()));
			saveList.add(entity);
			if(!imageurl.equals("")){//对图片是否进行图片展示进行处理
				List imagePlayList=imagePlayDomain.queryByCriteria(imagePlayDomain.getCriteria(true).add(Restrictions.eq("imageSource", "columnImage")).add(Restrictions.eq("columnId", entity.getColumnId())));
				String imagename="";
				BaseObject obj=resourceDomain.queryFirstByProperty("url", imageurl);
				if(obj!=null){
					imagename=obj.getTname();
				}
				for(int t=0;t<imagePlayList.size();t++){
					ImagePlay imagePlay=(ImagePlay)imagePlayList.get(t);
					ImagePlayRelationImages imagePlayRelationImages=(ImagePlayRelationImages)imagePlayRelationImagesDomain.getBaseObject();
					imagePlayRelationImages.setImagePlayId(imagePlay.getId());
					imagePlayRelationImages.setImageurl(imageurl);
					imagePlayRelationImages.setLinktext(entity.getTname());
					imagePlayRelationImages.setTname(imagename);
					imagePlayRelationImages.setLinkurl(linkurl);
					imagePlayRelationImages.setValid(true);
					this.saveBaseInfoToObject(request, imagePlayRelationImages);
					saveList.add(imagePlayRelationImages);
				}
			}
			this.baseDomain.save(saveList);
			WEBCONSTANTS.articleupdatestate=true;
			gotourl=request.getRequestURI()+"?columnId="+entity.getColumnId();
			this.outSuccessString(request, response, gotourl);
		}catch(Exception e){
			e.getStackTrace();
			gotourl=request.getRequestURI()+"?columnId="+entity.getColumnId();
			this.outFailString(request, response, WEBCONSTANTS.FAILINFOR, gotourl);
		}
		
	}
	/***
	 * 更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateform(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Article entity=(Article) baseDomain.query(id);
		String gotourl=request.getRequestURI()+"?page="+page+"&columnId="+entity.getColumnId();
		boolean auditState= entity.isAuditState();
		try{
			if(entity!=null){
				String imageurl="";
				List saveList=new ArrayList();
				List updateList=new ArrayList();
				String linkurl="web.do?action=articledetail&id="+entity.getId();
				this.bind(request, entity);
				String content=HttpUtil.getString(request, "articleContent", "");
				clobInfoDomain.update(entity.getId(), content);
				if(entity.getImgUrl()!=null&&!entity.getImgUrl().equals("")){
					imageurl=entity.getImgUrl();
				}
				if(!auditState&&entity.isAuditState()){//审核操作
					Users users=HttpUtil.getLoginUser(request);
					entity.setAuditer(users.getTname());
					entity.setAuditTime(new Date());
					entity.setPublishTime(new Date());
				}
				if(!imageurl.equals("")){//对图片是否进行图片展示进行处理
					List hasimage=imagePlayRelationImagesDomain.queryByCriteria(imagePlayRelationImagesDomain.getCriteria(true).add(Restrictions.eq("imageurl", imageurl)));
					if(hasimage.size()<1){
						String imagename="";
						BaseObject obj=resourceDomain.queryFirstByProperty("url", imageurl);
						if(obj!=null){
							imagename=obj.getTname();
						}
						List imagePlayList=imagePlayDomain.queryByCriteria(imagePlayDomain.getCriteria(true).add(Restrictions.eq("imageSource", "columnImage")).add(Restrictions.eq("columnId", entity.getColumnId())));
						for(int t=0;t<imagePlayList.size();t++){
							ImagePlay imagePlay=(ImagePlay)imagePlayList.get(t);
							ImagePlayRelationImages imagePlayRelationImages=(ImagePlayRelationImages)imagePlayRelationImagesDomain.getBaseObject();
							imagePlayRelationImages.setImagePlayId(imagePlay.getId());
							imagePlayRelationImages.setImageurl(imageurl);
							imagePlayRelationImages.setLinktext(entity.getTname());
							imagePlayRelationImages.setTname(imagename);
							imagePlayRelationImages.setLinkurl(linkurl);
							imagePlayRelationImages.setValid(true);
							this.saveBaseInfoToObject(request, imagePlayRelationImages);
							saveList.add(imagePlayRelationImages);
						}
					}
				}
				if(entity.isValid()){
					entity.setPublishTime(new Date());
				}
				updateList.add(entity);
				this.baseDomain.deleteAndSaveAndUpdate(null, saveList, updateList);
				WEBCONSTANTS.articleupdatestate=true;
				this.outSuccessString(request,response, gotourl);
			}
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
		return this.list(request, response);
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
		Article entity=(Article) baseDomain.query(id);
		String gotourl=request.getRequestURI()+"?page="+page+"&columnId="+entity.getColumnId();
		boolean auditState= entity.isAuditState();
		try{
			if(entity!=null){
				String imageurl="";
				List saveList=new ArrayList();
				List updateList=new ArrayList();
				String linkurl="web.do?action=articledetail&id="+entity.getId();
				this.bind(request, entity);
				String content=HttpUtil.getString(request, "articleContent", "");
				clobInfoDomain.update(entity.getId(), content);
				if(entity.getImgUrl()!=null&&!entity.getImgUrl().equals("")){
					imageurl=entity.getImgUrl();
				}
				if(!auditState&&entity.isAuditState()){//审核操作
					Users users=HttpUtil.getLoginUser(request);
					entity.setAuditer(users.getTname());
					entity.setAuditTime(new Date());
					entity.setPublishTime(new Date());
				}
				if(!imageurl.equals("")){//对图片是否进行图片展示进行处理
					List hasimage=imagePlayRelationImagesDomain.queryByCriteria(imagePlayRelationImagesDomain.getCriteria(true).add(Restrictions.eq("imageurl", imageurl)));
					if(hasimage.size()<1){
						String imagename="";
						BaseObject obj=resourceDomain.queryFirstByProperty("url", imageurl);
						if(obj!=null){
							imagename=obj.getTname();
						}
						List imagePlayList=imagePlayDomain.queryByCriteria(imagePlayDomain.getCriteria(true).add(Restrictions.eq("imageSource", "columnImage")).add(Restrictions.eq("columnId", entity.getColumnId())));
						for(int t=0;t<imagePlayList.size();t++){
							ImagePlay imagePlay=(ImagePlay)imagePlayList.get(t);
							ImagePlayRelationImages imagePlayRelationImages=(ImagePlayRelationImages)imagePlayRelationImagesDomain.getBaseObject();
							imagePlayRelationImages.setImagePlayId(imagePlay.getId());
							imagePlayRelationImages.setImageurl(imageurl);
							imagePlayRelationImages.setLinktext(entity.getTname());
							imagePlayRelationImages.setTname(imagename);
							imagePlayRelationImages.setLinkurl(linkurl);
							imagePlayRelationImages.setValid(true);
							this.saveBaseInfoToObject(request, imagePlayRelationImages);
							saveList.add(imagePlayRelationImages);
						}
					}
				}
				updateList.add(entity);
				this.baseDomain.deleteAndSaveAndUpdate(null, saveList, updateList);
				WEBCONSTANTS.articleupdatestate=true;
				this.outSuccessString(request,response, gotourl);
			}
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	
	public void setColumnsDomain(ColumnsDomain columnsDomain) {
		this.columnsDomain = columnsDomain;
		this.setBaseTreeDomain(columnsDomain);
	}
	public void setClobInfoDomain(ClobInfoDomain clobInfoDomain) {
		this.clobInfoDomain = clobInfoDomain;
	}
	public void setImagePlayDomain(ImagePlayDomain imagePlayDomain) {
		this.imagePlayDomain = imagePlayDomain;
	}
	public void setImagePlayRelationImagesDomain(
			ImagePlayRelationImagesDomain imagePlayRelationImagesDomain) {
		this.imagePlayRelationImagesDomain = imagePlayRelationImagesDomain;
	}
	public void setResourceDomain(ResourceDomain resourceDomain) {
		this.resourceDomain = resourceDomain;
	}
}
