package com.starsoft.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.cms.domain.ArticleDomain;
import com.starsoft.cms.entity.AcquisitionRule;
import com.starsoft.cms.entity.Article;
import com.starsoft.cms.entity.Columns;
import com.starsoft.cms.propertyEditor.ColumnsPropertyEditor;
import com.starsoft.core.util.HtmlParserUtil;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.domain.ClobInfoDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class AcquisitionRuleController extends BaseAjaxController implements BaseInterface {
	private ArticleDomain articleDomain;
	private ClobInfoDomain clobInfoDomain;
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
		List columnslist=baseDomain.queryByCriteria(DetachedCriteria.forClass(Columns.class).add(Restrictions.eq("valid", true)));
		model.put("columnslist", columnslist);
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
			List columnslist=baseDomain.queryByCriteria(DetachedCriteria.forClass(Columns.class).add(Restrictions.eq("valid", true)));
			model.put("columnslist", columnslist);
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
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				AcquisitionRule acquisitionRule=(AcquisitionRule)baseDomain.getBaseObject();
				String pageHrefStart=HttpUtil.getString(request, "pageHrefStart", "");
				String pageUrl=HttpUtil.getString(request, "pageUrl", "");
				String titleStart=HttpUtil.getString(request, "titleStart", "");
				String contentStart=HttpUtil.getString(request, "contentStart", "");
				acquisitionRule.setPageHrefStart(pageHrefStart);
				acquisitionRule.setContentStart(contentStart);
				acquisitionRule.setTitleStart(titleStart);
				acquisitionRule.setPageUrl(pageUrl);
				this.bind(request, acquisitionRule);
				this.saveBaseInfoToObject(request, acquisitionRule);
				this.baseDomain.save(acquisitionRule);
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR, gotourl);
			}
		}else{
			this.outFailString(request,response,"保存方式不对！请联系管理员", gotourl);
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
		gotourl=request.getRequestURI()+"?page="+page;
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				AcquisitionRule entity=(AcquisitionRule) baseDomain.query(id);
				String pageHrefStart=HttpUtil.getString(request, "pageHrefStart", "");
				String pageUrl=HttpUtil.getString(request, "pageUrl", "");
				String titleStart=HttpUtil.getString(request, "titleStart", "");
				String contentStart=HttpUtil.getString(request, "contentStart", "");
				entity.setPageHrefStart(pageHrefStart);
				entity.setContentStart(contentStart);
				entity.setTitleStart(titleStart);
				entity.setPageUrl(pageUrl);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR, gotourl);
			}
		}else{
			this.outFailString(request,response,"保存方式不对！请联系管理员", gotourl);
		}
	}
	/***
	 * 信息采集功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void startwork(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		AcquisitionRule acquisitionRule=(AcquisitionRule) baseDomain.query(id);
		if(acquisitionRule!=null){
			String url=acquisitionRule.getPageUrl();
			String startelement=acquisitionRule.getPageHrefStart();
			Columns columns=acquisitionRule.getColumns();
			String pageCharset=acquisitionRule.getPageCharset();
			if(pageCharset==null||pageCharset.equals("")){
				pageCharset="UTF-8";
			}
			List result=new ArrayList();
			if(startelement!=null&&!startelement.equals("")){//信息列表页面
				List hrefList=HtmlParserUtil.getHrefList(url, startelement,pageCharset);
				for(int t=0;t<hrefList.size();t++){
					String urlTemp=hrefList.get(t).toString();
					String titleStartTemp=acquisitionRule.getTitleStart();
					String cntentStartTemp=acquisitionRule.getContentStart();
					String title="";
					String content="";
					if(titleStartTemp!=null&&!titleStartTemp.equals("")){
						title=HtmlParserUtil.getParserContent(urlTemp, titleStartTemp,pageCharset);
					}
					if(cntentStartTemp!=null&&!cntentStartTemp.equals("")){
						content=HtmlParserUtil.getParserContent(urlTemp, cntentStartTemp,pageCharset);
					}
					Article article=(Article)articleDomain.getBaseObject();
					article.setTname(title);
					article.setColumnId(columns.getId());
					article.setHits(0);
					this.saveBaseInfoToObject(request, article);
					clobInfoDomain.save(article.getId(), content);
					if(!title.equals("")&&!content.equals("")){
						result.add(article);
					}
				
				}
			}else{//信息详细页面
				String titleStartTemp=acquisitionRule.getTitleStart();
				String contentStartTemp=acquisitionRule.getContentStart();
				String title="";
				String content="";
				String urls=acquisitionRule.getPageUrl();
				if(titleStartTemp!=null&&!titleStartTemp.equals("")){
					title=HtmlParserUtil.getParserContent(urls, titleStartTemp,pageCharset);
				}
				if(contentStartTemp!=null&&!contentStartTemp.equals("")){
					content=HtmlParserUtil.getParserContent(urls, contentStartTemp,pageCharset);
				}
				Article article=(Article)articleDomain.getBaseObject();
				article.setTname(title);
				article.setColumnId(columns.getId());
				article.setHits(0);
				this.saveBaseInfoToObject(request, article);
				clobInfoDomain.save(article.getId(), content);
				if(!title.equals("")&&!content.equals("")){
					result.add(article);
				}
			}
			baseDomain.save(result);
		}
		this.outSuccessString(request,response,"");
	}
	
	/***
	 * 绑定常用类型
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		if (binder != null) {
			binder.registerCustomEditor(Columns.class,
			new ColumnsPropertyEditor());
		}
		super.initBinder(request, binder);
	}
	public void setArticleDomain(ArticleDomain articleDomain) {
		this.articleDomain = articleDomain;
	}
	public void setClobInfoDomain(ClobInfoDomain clobInfoDomain) {
		this.clobInfoDomain = clobInfoDomain;
	}
}
