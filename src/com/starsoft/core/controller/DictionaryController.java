package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rmi.CORBA.ClassDesc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Dictionary;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.IdName;
/***
 * 字典管理
 * @author Administrator
 *
 */
public class DictionaryController extends BaseAjaxController implements BaseInterface {
	private JdbcTemplate jdbcTemplate;
	private AppesDomain appesDomain;
	
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria =baseDomain.getCriteria(null);
		String name=HttpUtil.getString(request, "tname","");
		if(!name.equals("")){
			criteria.add(Restrictions.ilike("tname", "%"+name+"%"));
		}
		String sortfield =HttpUtil.getString(request, "sortfield","id");
		boolean sortValue=HttpUtil.getBoolean(request, "sortvalue", false);
		if(sortValue){
			criteria.addOrder(Order.desc(sortfield));
		}else{
			criteria.addOrder(Order.asc(sortfield));
		}
		model.put("sortfield", sortfield);
		model.put("sortValue", sortValue);
		model.put("tname", name);
		List list=baseDomain.queryByCriteria(criteria, page);
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
		String itemId=HttpUtil.getString(request, "itemId", "");
		String gotourl="";
		Dictionary entity=(Dictionary)baseDomain.getBaseObject();
		gotourl=request.getRequestURI();
		if(gotourl.indexOf("?")>-1){
			gotourl=gotourl+"&itemId="+itemId;
		}else{
			gotourl=gotourl+"?itemId="+itemId;
		}
		try{
			this.bind(request, entity);
			this.saveBaseInfoToObject(request, entity);
			this.baseDomain.save(entity);
			this.outSuccessString(request,response, gotourl);
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
		try{
			Dictionary entity=(Dictionary) baseDomain.query(id);
			if(entity!=null){
				this.bind(request, entity);
			}
			gotourl=request.getRequestURI()+"?page="+page;
			this.baseDomain.update(entity);
			this.outSuccessString(request,response, gotourl);			
		}catch(Exception e){
			e.getStackTrace();
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showDictionary(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String id=HttpUtil.getString(request, "id", "");
		Boolean displayTitle=HttpUtil.getBoolean(request, "displayTitle",true);
		String defaultValue=HttpUtil.getString(request, "defaultValue", "");
		Dictionary dictionary=(Dictionary)this.baseDomain.query(id);
		String sdefaultValue = request.getParameter("defaultValue");
		List<IdName> list=new ArrayList<IdName>();
		model.put("displayTitle", displayTitle);
		if(dictionary!=null){
			if(dictionary.isValid()){//如果是静态
				String desc=dictionary.getDescription();
				list=this.dictionaryContentToList(desc);
			}else{//如果是动态
				String sql=dictionary.getDescription();
				list=this.dictionaryContentSqlToList(request,sql);
			}
			if(sdefaultValue==null&&defaultValue.equals("")&&dictionary.getDefaultValue()!=null&&!"".equals(dictionary.getDefaultValue())){
				defaultValue=dictionary.getDefaultValue();
			}
		}
		model.put("defaultValue", defaultValue);
		model.put("list", list);
		return new ModelAndView(this.getCustomPage("showDictionary"),model);
	}
	
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void showTnameFromId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		String appId=HttpUtil.getString(request, "appId", "");
		if(!id.equals("")&&!appId.equals("")){
			Appes appes=(Appes)appesDomain.query(appId);
			String content=(String)appesDomain.query(Class.forName(appes.getClassName()), appId);
			if(!StringUtil.isNullOrEmpty(content)){
				this.outJsonObject(response, content);
			}
		}
	}
	
	
	/***
	 * 通过sql语句查询数值
	 * @param request
	 * @param sql
	 * @return
	 */
	private List<IdName> dictionaryContentSqlToList(HttpServletRequest request,
			String sql) {
		List<IdName> result=new ArrayList<IdName>();
		String[] argsTemp=new String[100];
		int argsize=0;
		while(sql.contains(":")){//去除
			String subsqlparam=sql.substring(sql.indexOf(":"));
			if(subsqlparam.indexOf(" ")>-1){
				subsqlparam=subsqlparam.substring(0,subsqlparam.indexOf(" ")).trim();
			}else{
				subsqlparam=subsqlparam.trim();
			}
			String paramName=subsqlparam.replace(":","").trim();
			String paramValue = request.getParameter(paramName);
			if(paramValue!=null){
				argsTemp[argsize]=paramValue;
				argsize++;
				sql=sql.replaceFirst(subsqlparam,"?");
			}else{//清除该参数
				String beforsql=sql.substring(0,sql.indexOf(subsqlparam)).trim();
				if(beforsql.lastIndexOf("")>-1){
					beforsql=sql.substring(beforsql.lastIndexOf(" "), sql.indexOf(subsqlparam))+subsqlparam;
				}
				sql=sql.replaceFirst(beforsql.trim(),"").trim();
				if(sql.endsWith("where")){
					sql=sql+" 1=1";
				}
			}
		}
		String[] args=new String[argsize];
		if(argsize>0){
			for(int t=0;t<argsize;t++){
				args[t]=argsTemp[t];
			}
		}
		try{
			int maxRows=jdbcTemplate.getMaxRows();
			jdbcTemplate.setMaxRows(100);
			List<Map<String, Object>> list=jdbcTemplate.queryForList(sql, args);
			jdbcTemplate.setMaxRows(maxRows);
			for(Map<String, Object> map:list){
				String id=String.valueOf(map.get("id"));
				String name=String.valueOf(map.get("tname"));
				result.add(new IdName(id,name));
			}
		}catch(Exception e){
			logger.info("查询动态字典报错，sql语句="+sql);
		}
		return result;
	}
	/***
	 * 静态内容变成字典列表
	 * @param content
	 * @return
	 */
	private List<IdName> dictionaryContentToList(String content){
		List<IdName> result=new ArrayList<IdName>();
		List<String> items=StringUtil.toList(content, "#");
		for(String item:items){
			if(item.equals("")) continue;
			String itemId="";
			String itemName="";
			if(item.contains("(")){
				itemName=item.substring(0,item.indexOf("("));
				itemId=item.substring(item.indexOf("(")+1, item.length());
				if(itemId.endsWith(")")){
					itemId=itemId.substring(0, itemId.length()-1);
				}
			}else{
				itemId=	item;
				itemName=item;
			}
			result.add(new IdName(itemId,itemName));
		}
		return result;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}

}
