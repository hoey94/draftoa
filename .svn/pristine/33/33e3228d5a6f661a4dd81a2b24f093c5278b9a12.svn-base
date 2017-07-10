package com.starsoft.cms.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import com.starsoft.cms.domain.ArticleDomain;
import com.starsoft.cms.domain.LinksDomain;
import com.starsoft.cms.entity.Article;
import com.starsoft.cms.entity.ImagePlay;
import com.starsoft.cms.entity.ImagePlayRelationImages;
import com.starsoft.cms.entity.Links;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;

public class WebIndexController extends BaseAjaxController {
	private LinksDomain linksDomain;
	private ArticleDomain articleDomain;
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject userobj=(BaseObject)WebUtils.getSessionAttribute(request,"CURRERUSER");
		model.put("haslogin", true);
		if(userobj==null){//未登录
			model.put("haslogin", false);
		}
		return new ModelAndView(this.getListPage(),model);
	}
	/***
	 * 动态获取图片切换信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getImagePlayJson(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String playCode=HttpUtil.getString(request, "playCode", "");
		ImagePlay imagePlay=(ImagePlay)this.baseDomain.queryFirstByProperty(ImagePlay.class, "playCode", playCode);
		if(imagePlay!=null){
			StringBuilder jsonsb = new StringBuilder();
			jsonsb.append("{")
			.append("name:'").append(imagePlay.getTname()).append("',")  
			.append("playCode:'").append(playCode).append("',")  
			.append("width:").append(imagePlay.getWidth()).append(",")  
			.append("height:").append(imagePlay.getHeight()).append(",") 
			.append("playType:'").append(imagePlay.getPlayType()).append("',"); 
			Page page=new Page(0,6);
			if(imagePlay.getMaxImageNum()!=null){
				page=new Page(0,imagePlay.getMaxImageNum());
				jsonsb.append("maxImageNum:").append(imagePlay.getMaxImageNum()).append(","); 
			}
			jsonsb.append("rows: [");  
			List imageList=this.baseDomain.queryByCriteria(DetachedCriteria.forClass(ImagePlayRelationImages.class).add(Restrictions.eq("valid", true)).add(Restrictions.eq("imagePlayId", imagePlay.getId())), page);
			if(imageList.size()>0){
				for(int t=0;t<imageList.size();t++){
					ImagePlayRelationImages imagePlayRelationImages=(ImagePlayRelationImages)imageList.get(t);
					jsonsb.append("{");
					if(imagePlayRelationImages.getImageurl()!=null&&!imagePlayRelationImages.getImageurl().equals("")){
						jsonsb.append("name:'").append(imagePlayRelationImages.getTname()).append("',");
						jsonsb.append("linktext:'").append(imagePlayRelationImages.getLinktext()).append("',");
						jsonsb.append("linkurl:'").append(imagePlayRelationImages.getLinkurl()).append("',");
						jsonsb.append("imageurl:'").append(imagePlayRelationImages.getImageurl()).append("'");
					}
					jsonsb.append("}");
					if(t<imageList.size()-1){
						jsonsb.append(",");
					}
				}
			}
			jsonsb.append("]}"); 
			this.outSuccessString(request,response, jsonsb.toString());
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
	
	/***
	 * 查看首页底部部分列表信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView bottom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		this.cacheForSeconds(response, 120);
		List<Links> alllist=linksDomain.queryByCriteria(linksDomain.getCriteria(true),200);
		List imageList=new ArrayList();
		List hrcompanyList=new ArrayList();
		List gaoxiaoList=new ArrayList();
		List hrzhaopinList=new ArrayList();
		List jiaoyujiuyeList=new ArrayList();
		for(Links links:alllist){
			String  linkType=links.getLinkType();
			String imageurl=links.getImageurl();
			String url=links.getUrl();
			if("education".equals(linkType)&&!"".equals(url)){//教育部就业网站
				jiaoyujiuyeList.add(links);
			}else if("company".equals(linkType)&&!"".equals(url)){//用人单位网站
				hrcompanyList.add(links);
			}else if("hr".equals(linkType)&&!"".equals(url)){//招聘网站
				hrzhaopinList.add(links);
			}else if("Campus".equals(linkType)&&!"".equals(url)){//高校类网站
				gaoxiaoList.add(links);
			}else if("bottomimg".equals(linkType)&&!"".equals(imageurl)){//底部图片广告
				if(imageList.size()<16){
					imageList.add(links);
				}
			}else{//其他类的连接地址
				
			}
		}
		model.put("imageList", imageList);
		model.put("hrcompanyList", hrcompanyList);
		model.put("gaoxiaoList", gaoxiaoList);
		model.put("hrzhaopinList", hrzhaopinList);
		model.put("jiaoyujiuyeList", jiaoyujiuyeList);
		return new ModelAndView(this.getCustomPage("bottom"),model);
	}
	public void setArticleDomain(ArticleDomain articleDomain) {
		this.articleDomain = articleDomain;
	}
	public void setLinksDomain(LinksDomain linksDomain) {
		this.linksDomain = linksDomain;
	}
}
