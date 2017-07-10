package com.starsoft.cms.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.cms.domain.ArticleDomain;
import com.starsoft.cms.domain.ColumnsDomain;
import com.starsoft.cms.entity.Article;
import com.starsoft.cms.entity.Columns;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.domain.ClobInfoDomain;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.vo.IDNAMEMapper;
import com.starsoft.core.vo.IdName;
import freemarker.template.TemplateModelException;
/**
 * 内容管理系统文章列表和文章详细控制器
 * @author Administrator
 *
 */
public class WebArticleController extends BaseAjaxController {
	private ColumnsDomain columnsDomain;
	private ArticleDomain articleDomain;
	private ClobInfoDomain clobInfoDomain;
	private JdbcTemplate jdbcTemplate;
	/***
	 * 默认文章列表页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = HttpUtil.convertModel(request);
		String columnId=HttpUtil.getString(request, "columnId", "");
		Page page=HttpUtil.convertPage(request);
		List articlelist=new ArrayList();
		List<Columns> sublist=new ArrayList<Columns>();
		if(!columnId.equals("")){
			Columns columns=(Columns)columnsDomain.query(columnId);
			if(columns==null){
				return new ModelAndView(this.getCustomPage("columnsno"),model);
			}else{
				DetachedCriteria criteria=articleDomain.getCriteria(true);
				model.put("column", columns);
				model.put("defaultOrganName", columns.getTname());
				sublist=columnsDomain.queryByParentId(columnId, true, false);
				int sublistsize=sublist.size();
				if(sublistsize>0){
					List columnsId=new ArrayList();
					columnsId.add(columnId);
					for(int t=0;t<sublistsize;t++){
						Columns column=(Columns)sublist.get(t);
						columnsId.add(column.getId());
					}
					criteria.add(Restrictions.in("columnId", columnsId));
				}else{
					sublist=columnsDomain.queryByParentId(columns.getParentId(), true, false);
					criteria.add(Restrictions.eq("columnId", columnId));
				}
				criteria.addOrder(Order.desc("sortCode"));
				articlelist=articleDomain.queryByCriteria(criteria, page);
				model.put("page", page);
				model.put("articlelist", articlelist);
				model.put("sublist", sublist);
				return new ModelAndView(this.getCustomPage("columns"),model);
			}
		}else{
			return new ModelAndView(this.getCustomPage("columnsno"),model);
		}
		
	}
	/***
	 * 查看文章详细信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView articledetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String id=HttpUtil.getString(request, "id", "");
		Article article=(Article) articleDomain.query(id);
		model.put("article",article);
		String infoContent=clobInfoDomain.query(id);
		model.put("articleContent",infoContent);
		List<Columns> sublist=new ArrayList<Columns>();
		if(article!=null){
			article.setHits(article.getHits()+1);
			articleDomain.update(article);
			Columns columns=(Columns)columnsDomain.query(article.getColumnId());
			model.put("column", columns);
			model.put("defaultOrganName", columns.getTname());
			sublist=columnsDomain.queryByParentId(columns.getId(), true, false);
			int sublistsize=sublist.size();
			if(sublistsize<1){
				sublist=columnsDomain.queryByParentId(columns.getParentId(), true, false);
			}
			model.put("sublist", sublist);
			//取上一条数据
			IdName lastArticle=null;
			IdName nextArticle=null;
			
			String lastArticleSql="select id,tname from T_CMS_ARTICLE where columnId=? and id<? order by id desc limit 0,1";
			List<IdName> lastlist=jdbcTemplate.query(lastArticleSql,new Object[]{article.getColumnId(),id},new IDNAMEMapper());
			if(lastlist.size()>0){
				lastArticle=lastlist.get(0);
				model.put("lastArticle", lastArticle);
			}
			//取下一条数据
			String nextArticleSql="select id,tname from T_CMS_ARTICLE where columnId=? and id>? order by id asc limit 0,1";
			List<IdName> nextlist=jdbcTemplate.query(nextArticleSql,new Object[]{article.getColumnId(),id},new IDNAMEMapper());
			if(nextlist.size()>0){
				nextArticle=nextlist.get(0);
				model.put("nextArticle", nextArticle);
			}
			return new ModelAndView(this.getCustomPage("articledetail"),model);
		}else{
			return new ModelAndView(this.getCustomPage("articledetailno"),model);
		}
	}
	/***
	 * 静态话网页
	 * @param model
	 * @throws IOException
	 * @throws TemplateModelException
	 */
	public void updateclick(HttpServletRequest request,
				HttpServletResponse response) throws Exception {	
		String id=HttpUtil.getString(request, "id", "");
		if(!id.equals("")){
			Article	article=(Article)articleDomain.query(id);
			if(article!=null){
				article.setHits(article.getHits()+1);
				this.baseDomain.update(article);
			}
		} 
		
	}
	public void setColumnsDomain(ColumnsDomain columnsDomain) {
		this.columnsDomain = columnsDomain;
	}
	public void setArticleDomain(ArticleDomain articleDomain) {
		this.articleDomain = articleDomain;
	}
	public void setClobInfoDomain(ClobInfoDomain clobInfoDomain) {
		this.clobInfoDomain = clobInfoDomain;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
