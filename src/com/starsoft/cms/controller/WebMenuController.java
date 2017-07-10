package com.starsoft.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.cms.domain.ColumnsDomain;
import com.starsoft.cms.domain.WebMenuDomain;
import com.starsoft.cms.entity.WebMenu;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class WebMenuController extends BaseAjaxController implements BaseInterface {
	private ColumnsDomain columnsDomain;
	private WebMenuDomain webMenuDomain;
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView frame(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("title", "导航菜单");
		model.put("deleteAndAdd", "false");// 添加节点和删除节点的权利
		model.put("urlLink", "?action=list");
		model.put("addLink", "webMenu.do?action=add&parentId=");
		return new ModelAndView("baseframe/subframe",model);
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
		List parentlist=webMenuDomain.queryTreeByParentId(rootValue, 3, null, true);
		model.put("parentlist", parentlist);
		List columnlist=columnsDomain.queryTreeByParentId(rootValue, 4, true, false);
		model.put("columnlist", columnlist);
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
		List parentlist=webMenuDomain.queryTreeByParentId(rootValue, 3, true, true);
		model.put("parentlist", parentlist);
		List columnlist=columnsDomain.queryTreeByParentId(rootValue, 4, true, false);
		model.put("columnlist", columnlist);
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
		WebMenu entity=(WebMenu)baseDomain.getBaseObject();
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				this.bind(request, entity);
				entity.setTname(entity.getTname().trim());
				this.saveBaseInfoToObject(request, entity);
				if(entity.getParentId()==null||"".equals(entity.getParentId())){
					entity.setParentId(rootValue);
					entity.setSortCode(webMenuDomain.getMaxSortCode());
				}else{
					entity.setSortCode(webMenuDomain.getMaxSortCodeByProperty("parentId", entity.getParentId()));
				}
				this.baseDomain.save(entity);
				WEBCONSTANTS.articleupdatestate=true;
				gotourl=request.getRequestURI()+"?sortvalue=false&sortfield=sortCode";
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
		if("post".equals(method)){
			try{
				WebMenu entity=(WebMenu) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
					if("".equals(entity.getParentId())){
						entity.setParentId(rootValue);
					}
				}
				this.baseDomain.update(entity);
				gotourl=request.getRequestURI()+"?page="+page+"&sortvalue=false&sortfield=sortCode";
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
	public void setColumnsDomain(ColumnsDomain columnsDomain) {
		this.columnsDomain = columnsDomain;
	}
	public void setWebMenuDomain(WebMenuDomain webMenuDomain) {
		this.webMenuDomain = webMenuDomain;
	}
}
