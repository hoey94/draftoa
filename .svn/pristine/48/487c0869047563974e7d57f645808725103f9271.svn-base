package com.starsoft.cms.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.cms.domain.ArticleDomain;
import com.starsoft.cms.domain.ColumnsDomain;
import com.starsoft.cms.domain.ImagePlayRelationImagesDomain;
import com.starsoft.cms.domain.WebMenuDomain;
import com.starsoft.cms.entity.Article;
import com.starsoft.cms.entity.Columns;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.util.DateUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
/***
 * 网页静态化类
 * @author lenovo
 *
 */
public class StaticizeController extends BaseAjaxController{
	private ArticleDomain articleDomain;
	private ColumnsDomain columnsDomain;
	private WebMenuDomain webMenuDomain;
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
		model.put("page", page);
		return new ModelAndView(this.getListPage(),model);
	}
	/**
	 * 静态化首页
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView staticizeindexpage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List columnlist=columnsDomain.queryColumnsByParentId("", true);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("columnlist", columnlist);
		return new ModelAndView(this.getCustomPage("staticizeindexpage"),model);
	}
	/***
	 * 静态化首页操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void staticizeindex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//获取首页数据
		Map<String,Object> model = new HashMap<String,Object>();
		boolean top = HttpUtil.getBoolean(request, "top",false);
		boolean loginimagesnews = HttpUtil.getBoolean(request, "loginimagesnews",false);
		boolean center = HttpUtil.getBoolean(request, "center",false);
		boolean bottom = HttpUtil.getBoolean(request, "bottom",false);
		if(top){
//			model.put(arg0, arg1);
			List list=webMenuDomain.queryByCriteria(webMenuDomain.getCriteria(true).addOrder(Order.asc("sortCode")),new Page(0,11));
			model.put("list",list);
			this.staticpage(model, "top.htm", "html\\top.html");
		}
		if(loginimagesnews){
			//就业新闻相关信息
			List list=this.articleDomain.queryByColumnsIdAndSize("20130922180309946300747848526212", 12);
			model.put("list",list);
			//就业新闻相关图片
			List imagelist=imagePlayRelationImagesDomain.queryByCriteria(imagePlayRelationImagesDomain.getCriteria(true).add(Restrictions.eq("imagePlayId", "20130611214201918863421979787189")), new Page(0,6));
			model.put("imagelist",imagelist);
			this.staticpage(model, "loginimagesnews.htm", "html\\loginimagesnews.html");
		}
		if(center){
			//通知公告
			List tzgglist=this.articleDomain.queryByColumnsIdAndSize("20130922180159900306371142941840", 5);
			model.put("tzgglist",tzgglist);
			this.staticpage(model, "center.htm", "html\\center.html");
		}
		if(bottom){
			this.staticpage(model, "bottom.htm", "html\\bottom.html");
		}
		String rootproject=request.getContextPath();
		model.put("rootproject",rootproject);
		String filepath="html\\index.html";
		this.outSuccessString(request,response,"");
	}
	/**
	 * 静态化栏目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView staticizecolumn(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List columnlist=columnsDomain.queryColumnsByParentId("", true);
		Map<String,Object> model = new HashMap<String,Object>();
		String url = HttpUtil.getString(request, "url", "");
		model.put("columnlist", columnlist);
		return new ModelAndView(this.getCustomPage(url),model);
	}
	public ModelAndView staticizecontent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List columnlist=columnsDomain.queryByCriteria(columnsDomain.getCriteria(true, true));
		Map<String,Object> model = new HashMap<String,Object>();
		String url = HttpUtil.getString(request, "url", "");
		model.put("columnlist", columnlist);
		return new ModelAndView(this.getCustomPage(url),model);
	}
	/***
	 * 静态化栏目页面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void staticizecolumns(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String columnId=HttpUtil.getString(request, "columnId",rootValue);
		Columns column=(Columns)columnsDomain.query(columnId);
//		String columnTemplate=column.getTemplateList();
//		String webTempleturl="";
//		if("".equals(columnTemplate)){
//			webTempleturl="columns.htm";
//		}else{
//			WebTemplet webTemplet=(WebTemplet)commonDomain.query(WebTemplet.class,columnTemplate);
//			if(webTemplet!=null){
//				webTempleturl=webTemplet.getUrl();
//			}else{
//				webTempleturl="columns.htm";
//			}
//		}
		//获取首页数据
		List<Columns> columnsList=columnsDomain.queryByCriteria(columnsDomain.getCriteria(true, true).add(Restrictions.eq("displayIndex", true)));
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("menu", columnsList);
		String rootproject=request.getContextPath();
		model.put("rootproject",rootproject);		
		String filepath="html\\"+columnId+".html";
		this.outSuccessString(request,response,"");
	}
	/***
	 * 静态化栏目信息页面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void staticizecontents(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String columnId=HttpUtil.getString(request, "columnId",rootValue);
		String beginday=HttpUtil.getString(request, "beginday",DateUtil.formatDate(new Date()));
		Date beginDate=DateUtil.parseDate(beginday);
		Columns column=(Columns)columnsDomain.query(columnId);
//		String templateContent=column.getTemplateContent();
//		String webTempleturl="";
//		if("".equals(templateContent)){
//			webTempleturl="content.htm";
//		}else{
//			WebTemplet webTemplet=(WebTemplet)commonDomain.query(WebTemplet.class,templateContent);
//			if(webTemplet!=null){
//				webTempleturl=webTemplet.getUrl();
//			}else{
//				webTempleturl="content.htm";
//			}
//		}
		//获取导航数据
		List<Columns> columnsList=columnsDomain.queryByCriteria(columnsDomain.getCriteria(true, true));
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("menu", columnsList);
		String rootproject=request.getContextPath();
		model.put("rootproject",rootproject);
		//获取页面信息
		List list=articleDomain.queryByCriteria(articleDomain.getCriteria(true).add(Restrictions.eq("columnId", columnId)));	
		String projectPath = getServletContext().getRealPath("/");//获取文件路径 
		for(int t=0;t<list.size();t++){
			Article article=(Article)list.get(t);
			model.put("article",article);
			String createDate=article.getId().substring(0,10);
			File file=new File(projectPath+"html\\"+createDate+"\\");
			if(!file.exists()){
				file.mkdir();
			}
			String filepath="html\\"+createDate+"\\"+article.getId()+".html";
			try{
				String staticUrl=rootproject+"/html/"+createDate+"/"+article.getId()+".html";
				article.setStaticUrl(staticUrl);
				articleDomain.update(article);
			}catch(Exception e){
				e.getMessage();
			}
		}
		this.outSuccessString(request,response,"");
	}
	
	/***
	 * 生产静态页面
	 * @param model
	 * @param templateFilePath
	 * @param filePath
	 * @throws IOException
	 * @throws TemplateModelException
	 */
	public void staticpage(Map<String,Object> model,String templateFilePath,String filePath) throws IOException, TemplateModelException{
		Configuration cfg = new Configuration();
		String projectPath = getServletContext().getRealPath("/");//获取文件路径 
		String pageTempletpath="page\\webTemplet\\";
		cfg.setDirectoryForTemplateLoading(new File(projectPath+pageTempletpath));
		cfg.setOutputEncoding("UTF-8");
		Template template = cfg.getTemplate(templateFilePath);
		File file=new File(projectPath+filePath);
		file.deleteOnExit();
		try {
			template.process(model, new OutputStreamWriter(new FileOutputStream(file)));
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	public void setArticleDomain(ArticleDomain articleDomain) {
		this.articleDomain = articleDomain;
	}
	public void setColumnsDomain(ColumnsDomain columnsDomain) {
		this.columnsDomain = columnsDomain;
	}
	public void setWebMenuDomain(WebMenuDomain webMenuDomain) {
		this.webMenuDomain = webMenuDomain;
	}
	public void setImagePlayRelationImagesDomain(
			ImagePlayRelationImagesDomain imagePlayRelationImagesDomain) {
		this.imagePlayRelationImagesDomain = imagePlayRelationImagesDomain;
	}
}
