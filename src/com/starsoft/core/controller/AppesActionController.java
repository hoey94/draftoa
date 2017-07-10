package com.starsoft.core.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.AppesAction;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class AppesActionController extends BaseAjaxController implements BaseInterface {
	AppesDomain appesDomain;
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
		String app=HttpUtil.getString(request, "app","");
		if(!app.equals("")){
			criteria.add(Restrictions.eq("appes", app));
			Appes appes=(Appes)appesDomain.query(app);
			model.put("appes", appes);
			List saveList=new ArrayList();
			Object obj=null;
			try{
				if(appes!=null&&appes.getControllerClassName()!=null&&!appes.getControllerClassName().equals("")){
					obj=Class.forName(appes.getControllerClassName()).newInstance();
				}
			}catch(Exception e){
				logger.info("============控制器不存在========="+appes.getControllerClassName());
			}
			
			if(obj==null&&this.getApplicationContext().containsBean(app+"Controller")){
				obj=this.getApplicationContext().getBean(app+"Controller");
				if(obj!=null){
					appes.setControllerClassName(obj.getClass().getName());
					this.baseDomain.update(appes);
				}
			}
			if(obj!=null&&obj instanceof BaseAjaxController){// 如果是默认系统级别控制器
				Method[] methods=obj.getClass().getDeclaredMethods();
				Map templeMAap=new HashMap();
				templeMAap.put("save", "保存操作_form");
				templeMAap.put("list", "列表操作_list");
				templeMAap.put("add", "新增操作_list");
				templeMAap.put("update", "更新操作_form");
				templeMAap.put("edit", "编辑操作_list");
				templeMAap.put("jsdelete", "标记删除操作_list");
				templeMAap.put("delete", "删除操作_list");
				int sortCode=1;
				for(Object o : templeMAap.keySet()){
					String actionName=o.toString();
					List lists=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("tname", actionName)).add(Restrictions.eq("appes", app)));
					if(lists.size()>0) break;
					String actionValue=templeMAap.get(o).toString();  
					String description=actionValue.substring(0, actionValue.indexOf("_"));
					String actionCate=actionValue.substring(actionValue.indexOf("_")+1);
					AppesAction appesAction=(AppesAction)this.baseDomain.getBaseObject();
					appesAction.setTname(actionName);
					appesAction.setAppes(appes.getId());
					appesAction.setActionType(actionCate);
					appesAction.setValid(true);
					sortCode++;
					this.saveBaseInfoToObject(request, appesAction);
					saveList.add(appesAction);
				}
				for(int s=0;s<methods.length;s++){
					Method method=methods[s];
					String methodName=method.getName();
					if(methodName.startsWith("set")||templeMAap.containsKey(methodName)||"initBinder".equals(templeMAap)) continue;
					List lists=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.eq("tname", methodName)).add(Restrictions.eq("appes", app)));
					if(lists.size()>0) continue;
					AppesAction appesAction=(AppesAction)this.baseDomain.getBaseObject();
					appesAction.setTname(methodName);
					appesAction.setAppes(appes.getId());
					appesAction.setActionType("list");
					appesAction.setValid(true);
					sortCode++;
					this.saveBaseInfoToObject(request, appesAction);
					saveList.add(appesAction);
				}
				if(saveList.size()>0){
					baseDomain.save(saveList);
					appesDomain.updateCache(app);
				}
			}else{//自定义控制器
				System.out.println("-------------obj.getClass().getName------------");
			}
		}
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		model.put("app", app);
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
		BaseObject entity=baseDomain.getBaseObject();
		String method=request.getMethod().toLowerCase();
		String gotourl=request.getRequestURI();
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
		if("post".equals(method)){
			try{
				BaseObject entity=(BaseObject) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				gotourl=request.getRequestURI()+"?page="+page;
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}
}
