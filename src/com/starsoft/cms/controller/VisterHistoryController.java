package com.starsoft.cms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.cms.domain.VisterNumberDomain;
import com.starsoft.cms.entity.VisterHistory;
import com.starsoft.cms.entity.VisterNumber;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.DateUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class VisterHistoryController extends BaseAjaxController implements BaseInterface {
	private VisterNumberDomain visterNumberDomain;
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
		String locahref=HttpUtil.getString(request, "locahref","");
		try{
			Users loginUser = HttpUtil.getLoginUser(request);
			VisterHistory visterHistory = new VisterHistory();
			String path = request.getRequestURI();
			String query=request.getQueryString();
			if(query!=null&&query.equals("")){
				path += "?" + query;
			}
       		if(loginUser!=null){
       			visterHistory.setCreateId(loginUser.getId());
       			visterHistory.setTname(loginUser.getTname());
       		}else{
       			visterHistory.setTname("未登录用户");
       		}
       		visterHistory.setIp(request.getRemoteAddr());
       		visterHistory.setBrowserType(HttpUtil.getBrowserType(request));
       		visterHistory.setIp(request.getRemoteAddr());
       		visterHistory.setSessionId(request.getSession().getId());
       		visterHistory.setValid(true);
       		visterHistory.setVisterpath(locahref);
			this.baseDomain.save(visterHistory);
			VisterNumber visterNumber=(VisterNumber)visterNumberDomain.query(rootValue);
			if(visterNumber!=null){
				visterNumber.addVisterHistory();
				visterNumberDomain.update(visterNumber);
			}else{
				visterNumber=new VisterNumber();
				visterNumber.setId(rootValue);
				this.saveBaseInfoToObject(request, visterNumber);
				visterNumber.addVisterHistory();
				visterNumber.setTname(DateUtil.formatDate(new Date()));
				visterNumberDomain.save(visterNumber);
			}
			this.outJsonObject(response, visterNumber);
		}catch(Exception e){
			e.getStackTrace();
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
	public void setVisterNumberDomain(VisterNumberDomain visterNumberDomain) {
		this.visterNumberDomain = visterNumberDomain;
	}
}
