package com.starsoft.core.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javassist.Modifier;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.core.domain.AppesAttributeDomain;
import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.domain.MenuDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.AppesAction;
import com.starsoft.core.entity.AppesAttribute;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.Menu;
import com.starsoft.core.util.DateUtil;
import com.starsoft.core.util.FileUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.IDNAMEMapper;
import com.starsoft.core.vo.IdName;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class AppesController extends BaseAjaxController implements BaseInterface {
	private AppesAttributeDomain appesAttributeDomain;
	private AppesDomain appesDomain;
	private MenuDomain menuDomain;
	private String  subSystem;
	private JdbcTemplate jdbcTemplate;
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
		criteria.add(Restrictions.or(Restrictions.eq("subSystemName", this.getSubSystem()), Restrictions.or(Restrictions.eq("subSystemName", "edu"), Restrictions.eq("subSystemName", "custom"))));
		String sortfield =HttpUtil.getString(request, "sortfield","createTime");
		boolean sortValue=HttpUtil.getBoolean(request, "sortvalue", true);
		criteria.addOrder(Order.desc("createTime"));
		model.put("sortfield", sortfield);
		model.put("sortValue", sortValue);
		List list= baseDomain.queryByCriteria(criteria,page);
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
		String id=StringUtil.getUUID();
		model.put("id", id);
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
		String gotourl="";
		String method=request.getMethod().toLowerCase();
		String id=HttpUtil.getString(request, "id", "");
		String tableName=HttpUtil.getString(request, "tableName", "");
		if("post".equals(method)){
			try{
				BaseObject entity=(BaseObject) baseDomain.query(id);
				if(entity==null){
					entity=baseDomain.getBaseObject();
					List list=baseDomain.queryByProperty("tableName", tableName);
					if(list.size()>0){
						this.outFailString(request,response, "表名已经占用!",gotourl);
					}else{
						this.bind(request, entity);
						this.saveBaseInfoToObject(request, entity);
						gotourl=request.getRequestURI();
						this.baseDomain.save(entity);
						this.outSuccessString(request,response, gotourl);
					}
				}else{
					this.outFailString(request,response, "唯一标识已经被占用!",gotourl);
				}
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
	/***
	 * 生成配置文件以及java类
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void initinfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			List ids=HttpUtil.getList(request, "ids", ";");
			List list=baseDomain.queryByCriteria(baseDomain.getCriteria(null).add(Restrictions.in("id", ids)));
			int entitySize=list.size();
			Menu rootmenu=(Menu) menuDomain.query(rootValue);
			if("custom".equals(subSystem)){
				Map root = new HashMap();
				root.put("initkey", subSystem);
				for(int t=0;t<entitySize;t++){
					Appes entity=(Appes) list.get(t);			
					List menus=menuDomain.queryByProperty("module", entity.getId());
					if(menus.size()==0){
						//生成对应的菜单
						Menu menu=new Menu();
						menu.setTname(entity.getTname());
						menu.setSortCode(menuDomain.getMaxSortCode());
						menu.setValid(Boolean.TRUE);
						menu.setModule(entity.getId());
						menu.setParentId("commonapp");
						menu.setUrl("commonapp.do?app="+entity.getId());
						menu.setMenuType("function");
						menu.setTargets("mainarea");
						this.saveBaseInfoToObject(request, menu);
						menuDomain.save(menu);
					}
				}
			}else{
				List menulist=menuDomain.queryByProperty("module", subSystem);
				Menu initkeymenu=null;
				if(menulist.size()>0){
					initkeymenu=(Menu)menulist.get(0);
				}else{
					initkeymenu=new Menu();
					initkeymenu.setTname(subSystem);
					initkeymenu.setSortCode(menuDomain.getMaxSortCode());
					initkeymenu.setValid(Boolean.TRUE);
					initkeymenu.setModule(subSystem);
					initkeymenu.setParentId(rootValue);
					initkeymenu.setMenuType("structure");
					initkeymenu.setTargets("leftarea");
					this.saveBaseInfoToObject(request, initkeymenu);
					menuDomain.save(initkeymenu);
				}
				if(entitySize>0){
					String projectPath=getServletContext().getRealPath("/");
					Configuration cfg = new Configuration();
					cfg.setDirectoryForTemplateLoading(new File(projectPath+"templates"));
					cfg.setOutputEncoding("UTF-8");
					//生成控制器配置信息信息
					for(int t=0;t<entitySize;t++){
						Appes entity=(Appes) list.get(t);
						entity.setValid(true);
						entity.setControllerClassName("com.starsoft."+subSystem+".controller."+StringUtil.getSFirstDaxie(entity.getId())+"Controller");
						this.baseDomain.update(entity);
					}
					Map rootxml = new HashMap();
					rootxml.put("initkey", subSystem);
					rootxml.put("list", list);
					File file=new File(projectPath+"WEB-INF\\config\\controller-"+subSystem+".xml");
					if(file.exists()){
						String dateTime=DateUtil.getWeekOfDate(new Date());
						File newfile=new File(projectPath+"WEB-INF\\config\\controller-"+subSystem+dateTime+".xml");
						Template template = cfg.getTemplate("Controller-core.xml");
						template.process(rootxml, new OutputStreamWriter(new FileOutputStream(newfile)));
						System.out.println("新创建配置文件："+newfile.getName());
					}else{
						file=new File(projectPath+"WEB-INF\\config\\controller-"+subSystem+".xml");
						Template template = cfg.getTemplate("Controller-core.xml");
						template.process(rootxml, new OutputStreamWriter(new FileOutputStream(file)));
					}
					Map root = new HashMap();
					root.put("initkey", subSystem);
					for(int t=0;t<entitySize;t++){
						Appes entity=(Appes) list.get(t);
						String upprojectPath=projectPath.substring(0, projectPath.indexOf("WebContent"));
						String domainFileName=upprojectPath+entity.getClassPath().replace("entity", "domain").replace(".java", "")+"Domain.java";
						String domainImplFileName=upprojectPath+entity.getClassPath().replace("entity", "domainImpl").replace(".java", "")+"DomainImpl.java";
						String controllerFileName=upprojectPath+entity.getClassPath().replace("entity", "controller").replace(".java", "")+"Controller.java";
						// 生成domain 接口
						File domainfile=new File(domainFileName);
					    root.put("name", entity.getId());
					    root.put("fullName", entity.getClassName());
					    root.put("path", entity.getClassPath());
					    Template template = cfg.getTemplate("Domain.java");
						if(domainfile.exists()){
							System.out.println("已经存在domain接口："+domainfile.getName());
						}else{
							File fileforder=domainfile.getParentFile();
							if(!fileforder.exists()){
								fileforder.mkdirs();
							}
							template.process(root, new OutputStreamWriter(new FileOutputStream(domainfile)));
						}
						//生成domain实现类
						File domainImplfile=new File(domainImplFileName);
						if(domainImplfile.exists()){
							System.out.println("已经存在domainImpl实现类："+domainImplfile.getName());
						}else{
							template = cfg.getTemplate("DomainImpl.java");
							File fileforder=domainImplfile.getParentFile();
							if(!fileforder.exists()){
								fileforder.mkdirs();
							}
							template.process(root, new OutputStreamWriter(new FileOutputStream(domainImplfile)));
						}
						//生成控制器
						File controllerfile=new File(controllerFileName);
						if(controllerfile.exists()){
							System.out.println("已经存在控制器实现类："+controllerfile.getName());
						}else{
							template = cfg.getTemplate("Controller.java");
							File fileforder=controllerfile.getParentFile();
							if(!fileforder.exists()){
								fileforder.mkdirs();
							}
							template.process(root, new OutputStreamWriter(new FileOutputStream(controllerfile)));
						}
						List menus=menuDomain.queryByProperty("module", entity.getId());
						if(menus.size()==0){
							//生成对应的菜单
							Menu menu=new Menu();
							menu.setTname(entity.getTname());
							menu.setSortCode(menuDomain.getMaxSortCode());
							menu.setValid(Boolean.TRUE);
							menu.setModule(entity.getId());
							menu.setParentId(initkeymenu.getId());
							menu.setUrl(entity.getId()+".do");
							menu.setMenuType("function");
							menu.setTargets("mainarea");
							this.saveBaseInfoToObject(request, menu);
							menuDomain.save(menu);
						}
					}
				}
			}
	}
	/**
	 * 彻底删除对象功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<String> ids=HttpUtil.getList(request, "ids", ";");
		String method=request.getMethod().toLowerCase();
		for(int t=0;t<ids.size();t++){
			String id=ids.get(t).toString();
			Appes appes=(Appes)baseDomain.query(id);
			String hql = "delete from "+AppesAttribute.class.getName()+" where appes = ?";
			String hqlAction = "delete from "+AppesAction.class.getName()+" where appes = ?";
			String hqlAAction = "delete from "+AppesAttribute.class.getName()+" where appes = ?";
			List paras=new ArrayList();
			paras.add(id);
			baseDomain.executeByHQL(hql, paras);
			baseDomain.executeByHQL(hqlAction, paras);
			baseDomain.delete(appes);
		}
		this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
	}
	/***
	 * 重新加载java 类文件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void reloadClass(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			String projectPath=getServletContext().getRealPath("/");
			String javaPackagePath=projectPath.substring(0, projectPath.indexOf("WebContent"))+"\\src\\com\\starsoft\\"+this.getSubSystem()+"\\entity";
			File directory = new File(javaPackagePath);
			if(!directory.exists()){
				directory.mkdirs();
			}
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++ ){
				String fileName=files[i].getName();
				String className=fileName.substring(0, fileName.length()-5);
				if(!fileName.endsWith(".java")||"BaseObject".equals(className)||"BaseTreeObject".equals(className)){
					continue;
				}
				
				if(className.equals("ChapterMode")){
					String oString = className;
				}
				String fullName="com.starsoft."+this.getSubSystem()+".entity."+className;
				String code=className.substring(0,1).toLowerCase()+className.substring(1, className.length());
				String path="\\src\\com\\starsoft\\"+this.getSubSystem()+"\\entity"+"\\"+fileName;
				Class<?> clsTemp = Class.forName(fullName);
				int mod=clsTemp.getModifiers();
				if(Modifier.isAbstract(mod)||Modifier.isInterface(mod)){
					continue;
				}
				Appes appes=(Appes)baseDomain.query(code);
				if(appes!=null){
					appes.setClassName(fullName);
					appes.setClassPath(path);
					appes.setSubSystemName(this.getSubSystem());
					Class<?> cls = Class.forName(fullName);
					boolean flag = cls.isAnnotationPresent(InitNameAnnotation.class);
					if (flag) {
						InitNameAnnotation annotation = cls.getAnnotation(InitNameAnnotation.class);
						appes.setTname(annotation.value());
					}
					flag = cls.isAnnotationPresent(Table.class);
					if (flag) {
						Table annotation = cls.getAnnotation(Table.class);
						appes.setTableName(annotation.name());
					}
					baseDomain.update(appes);
				}else{
					try{
						appes=new Appes();
						appes.setId(code);
						appes.setClassName(fullName);
						appes.setClassPath(path);
						appes.setValid(true);
						appes.setSubSystemName(this.getSubSystem());
						Class<?> cls = Class.forName(fullName);
						boolean flag = cls.isAnnotationPresent(InitNameAnnotation.class);
						if (flag) {
							InitNameAnnotation annotation = cls.getAnnotation(InitNameAnnotation.class);
							appes.setTname(annotation.value());
						}else{
							continue;
						}
						flag = cls.isAnnotationPresent(Table.class);
						if (flag) {
							Table annotation = cls.getAnnotation(Table.class);
							appes.setTableName(annotation.name());
						}
						this.saveBaseInfoToObject(request, appes);
						baseDomain.save(appes);
						//bean 属性管理增加默认基础属性
						Field[] basefields=BaseObject.class.getDeclaredFields();
						int sortCode=appesAttributeDomain.getMaxSortCodeByProperty("appes", appes.getId());
						for(Field field :basefields){
							AppesAttribute obj=new AppesAttribute();
							String attributeType=field.getType().getSimpleName();
							if(field.isAnnotationPresent(InitFieldAnnotation.class)){ 
					        	   InitFieldAnnotation initFieldAnnotation=field.getAnnotation(InitFieldAnnotation.class);
					        	   obj.setDescription(initFieldAnnotation.value());
					        	   if(!initFieldAnnotation.maxlength().equals("")){
					        		   obj.setMaxLength(Integer.valueOf(initFieldAnnotation.maxlength()));
					        	   }
					        	   obj.setTname(field.getName());
						           obj.setAttributeType(attributeType);
						           obj.setAddDisplay(true);
						           obj.setEditDisplay(true);
						           obj.setValid(true);
						           obj.setListDisplay(true);
						           obj.setAppes(appes.getId());
						           obj.setMaxLength(255);
						           if("tname".equals(field.getName())){
						        	   obj.setNullValue(false);
						        	   obj.setAddDisplay(true);
						        	   obj.setMaxLength(512);
						           }else{
						        	   obj.setNullValue(true);
						           }
						           obj.setSortCode(sortCode);
						           if("sortCode".equals(field.getName())){
						        	   obj.setAddDisplay(false);
						        	   obj.setMaxLength(10);
						        	   obj.setSortCode(sortCode+100);
						           }else if("valid".equals(field.getName())){
						        	   obj.setAddDisplay(false);
							           obj.setEditDisplay(false);
							           obj.setMaxLength(5);
							           obj.setSortCode(sortCode+110);
						           }else if("id".equals(field.getName())){
						        	   obj.setAddDisplay(false);
							           obj.setEditDisplay(false);
							           obj.setListDisplay(false);
							           obj.setMaxLength(32);
						           }else if("createId".equals(field.getName())){
						        	   obj.setAddDisplay(false);
							           obj.setEditDisplay(false);
							           obj.setListDisplay(true);
							           obj.setMaxLength(32);
							           obj.setSortCode(sortCode+90);
						           }
						           if(attributeType.equals("Date")||attributeType.equals("java.util.Date")){
						        	   obj.setDisplayType("date");
						        	   obj.setMaxLength(20);
						           }else if(attributeType.equals("Integer")||attributeType.equals("java.lang.Integer")){
						        	   obj.setDisplayType("int");
						        	   obj.setMaxLength(10);
						           }else if(attributeType.equals("boolean")||attributeType.equals("Boolean")){
						        	   obj.setDisplayType("select");
						           }else{
						        	   obj.setDisplayType("str");
						           }
						           if("description".equals(field.getName())){
						        	   obj.setDisplayType("textarea");
						        	   obj.setListDisplay(false);
						           }
						           this.saveBaseInfoToObject(request, obj);
						           baseDomain.save(obj);
						           sortCode++;
					         }
						} 
						if(cls.newInstance() instanceof BaseTreeObject){
							   sortCode++;
					           AppesAttribute obj=new AppesAttribute();
				        	   obj.setDescription("上级标识");
			        		   obj.setMaxLength(32);
					           obj.setTname("parentId");
					           obj.setAttributeType("String");
					           obj.setAddDisplay(true);
					           obj.setEditDisplay(true);
					           obj.setValid(false);
				        	   obj.setListDisplay(false);
					           obj.setAppes(appes.getId());
					           obj.setMaxLength(255);
				        	   obj.setDisplayType("str");
				        	   obj.setSortCode(sortCode+70);
					           this.saveBaseInfoToObject(request, obj);
					           baseDomain.save(obj);
						}
						Field[] fields = cls.getDeclaredFields();
						for(Field field :fields){       
					           sortCode++;
					           AppesAttribute obj=new AppesAttribute();
					           if(field.isAnnotationPresent(InitFieldAnnotation.class)){ 
					        	   InitFieldAnnotation initFieldAnnotation=field.getAnnotation(InitFieldAnnotation.class);
					        	   obj.setDescription(initFieldAnnotation.value());
					        	   if(!initFieldAnnotation.maxlength().equals("")){
					        		   obj.setMaxLength(Integer.valueOf(initFieldAnnotation.maxlength()));
					        	   }
					        	   String attributeType=field.getType().getSimpleName();
						           obj.setTname(field.getName());
						           obj.setAttributeType(attributeType);
						           obj.setAddDisplay(true);
						           obj.setEditDisplay(true);
						           obj.setValid(false);
						           obj.setListDisplay(true);
						           obj.setAppes(appes.getId());
						           obj.setMaxLength(255);
						           if(attributeType.equals("Date")||attributeType.equals("java.util.Date")){
						        	   obj.setDisplayType("date");
						        	   obj.setMaxLength(20);
						           }else if(attributeType.equals("Integer")||attributeType.equals("java.lang.Integer")){
						        	   obj.setDisplayType("int");
						        	   obj.setMaxLength(10);
						           }else if(attributeType.equals("boolean")||attributeType.equals("Boolean")){
						        	   obj.setDisplayType("select");
						           }else{// 此处可增加对象类型
						        		obj.setDisplayType("str");
						           }
						           if("parentId".equals(field.getName())){
						        	   obj.setListDisplay(false);
						        	   obj.setDisplayType("select");
						           }
						           obj.setSortCode(sortCode);
						           this.saveBaseInfoToObject(request, obj);
						           baseDomain.save(obj);
					           }
						}  
					}catch(Exception e){
						continue;
					}
				}
			}
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
	}
	/***
	 * 生成页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void initpageinfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			List<String> ids=HttpUtil.getList(request, "ids", ";");
			for(String id:ids){
				Appes appes=appesDomain.getAppes(id);
				Map map = new HashMap();
				map.put("appes", appes);
				map.put("importcontent", "<#import \"../../macro/macrocommon.htm\" as common>");
				map.put("headercontent", "<@common.header/>");
				map.put("addheadercontent", "<@common.addheader name=\\"+appes.getTname()+"\"/>");
				map.put("readheadercontent", "<@common.readheader name=\\"+appes.getTname()+"\"/>");
				map.put("editheadercontent", "<@common.editheader name=\\"+appes.getTname()+"\"/>");
				map.put("listheadercontent", "<@common.listheader name=\\"+appes.getTname()+"\"/>");
				map.put("paginationcontent", "<#import \"../../macro/paginationCommon.htm\" as pagination>");
				map.put("bottomcontent", "<@common.bottom/>");
				String projectPath=getServletContext().getRealPath("/");
				String folderPath=projectPath+"page\\"+appes.getSubSystemName()+"\\"+appes.getId();
				if(!new File(folderPath).isDirectory()){
					new File(folderPath).mkdirs();
				}
				String nowtime=DateUtil.getCurDateTimeSend();
				if(!new File(folderPath).isDirectory()){
					new File(folderPath).mkdirs();
				}
				Appes entity=appesDomain.getAppes(id);
				File readpage=new File(folderPath+"\\"+entity.getId()+"Read.htm");
				if(readpage.exists()){
					readpage.renameTo(new File(folderPath+"\\"+nowtime+entity.getId()+"ReadNew.htm"));
					readpage=null;
				}
				generateReadPage(folderPath+"\\"+entity.getId()+"Read.htm",entity);
				File listpage=new File(folderPath+"\\"+entity.getId()+"List.htm");
				if(listpage.exists()){
					listpage.renameTo(new File(folderPath+"\\"+nowtime+entity.getId()+"ListNew.htm"));
					listpage=null;
				}
				generateListPage(folderPath+"\\"+entity.getId()+"List.htm",entity);
				File addpage=new File(folderPath+"\\"+entity.getId()+"Add.htm");
				if(addpage.exists()){
					addpage.renameTo(new File(folderPath+"\\"+nowtime+entity.getId()+"AddNew.htm"));
					addpage=null;
				}
				generateAddPage(folderPath+"\\"+entity.getId()+"Add.htm",entity);
				File editpage=new File(folderPath+"\\"+entity.getId()+"Edit.htm");
				if(editpage.exists()){
					editpage.renameTo(new File(folderPath+"\\"+entity.getId()+"Edit.htm"));
					editpage=null;
				}
				generateEditPage(folderPath+"\\"+entity.getId()+"Edit.htm",entity);
			}
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
		}
	/***
	 * 生成列表页面
	 * @param filePath
	 * @param entity
	 * @throws Exception
	 */
	private void generateListPage(String filePath, Appes entity) throws Exception {
		StringBuilder content=new StringBuilder();
		content.append("<#import \"../../macro/macrocommon.htm\" as common>\n");
		content.append("<#import \"../../macro/paginationCommon.htm\" as pagination>\n");
		content.append("<@common.header/>\n");
		content.append("<@common.listheader name=\""+entity.getTname()+"\"/>\n");
		content.append("<table  cellSpacing=1 cellPadding=2 width=\"100%\" align=center bgColor=#cccccc border=0 class=\"tab\">\n");
		content.append("<form id=\"commonform\" action=\"?action=list\" method=\"post\">\n");
		content.append("<input type=\"hidden\" id=\"sortfield\" name=\"sortfield\" value=\"${sortfield?default('')}\" />\n");
		content.append("<input type=\"hidden\" id=\"sortvalue\" name=\"sortvalue\" value=\"${sortvalue?default('')}\" />\n");
		content.append("<input type='hidden' id='page' name='page' value='${page.pageIndex?c}' />\n");
		content.append("<tr>\n");
		content.append("<td bgColor=#f8f8f8 height=22 width=100>请选择筛选条件</td>\n");
		content.append("<td bgColor=#f8f8f8>"+entity.getTname()+"名称：<input class=\"txtinput\"  id=\"tname\" name=\"tname\" value=\"${tname?default('')}\"></td>\n");
		content.append("<td bgColor=#f8f8f8 width=60><input type=\"image\" id=\"ImageButton1\" src=\"theme/defaultstyle/images/search.gif\" onclick=\"searchSubmit()\"	border=\"0\" /></td>\n");
		content.append("</tr>\n");
		content.append("</form>\n");
		content.append("</table>\n");
		content.append("<table cellSpacing=1 cellPadding=2 width=\"100%\" align=center bgColor=#cccccc border=0 class=\"tab\">\n");
		content.append("<thead>\n");
		content.append("<tr>\n");
		content.append("<td width=20><input type=\"checkbox\" name=\"allbox\" value=\"checkbox\" onClick=\"selectAll(this.checked)\" /></td>\n");
		List list=appesAttributeDomain.queryByCriteria(appesAttributeDomain.getCriteria(null).add(Restrictions.eq("appes", entity.getId())).add(Restrictions.eq("listDisplay", true)));
		for(int t=0;t<list.size();t++){
			AppesAttribute entityAttribute=(AppesAttribute) list.get(t);
			content.append("<td onclick=\"resetSort('"+entityAttribute.getTname()+"')\" style=\"cursor:pointer;\" >");
			content.append(entityAttribute.getDescription());
			content.append("<span id=\"sort"+entityAttribute.getTname()+"\"></span></td>\n");
		}
		content.append("</tr>\n");
		content.append("</thead>\n");
		content.append("<tbody>\n");
		content.append("<#list list as obj>\n");
		content.append("<tr>\n");
		content.append("<td width=20 align=\"center\"><input type=\"checkbox\" name=\"box\" value=\"${obj.id?default('')}\"/></td>\n");
		for(int t=0;t<list.size();t++){
			AppesAttribute entityAttribute=(AppesAttribute) list.get(t);
			content.append("<td bgColor=#ffffff>");
			String attributeType=entityAttribute.getAttributeType();
			if("String".equals(attributeType)){
				if("tname".equals(entityAttribute.getTname())){
					content.append("<a href='javascript:void(0)' onClick=\"editInfor('${obj.id}')\" >");
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					content.append("?default('')}");
					content.append("</a>");
				}else{
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					content.append("?default('')}");
				}
			}else if("int".equals(attributeType)){
				content.append("${obj.");
				content.append(entityAttribute.getTname());
				content.append("?c}");
			}else if("Integer".equals(attributeType)){
				content.append("${obj.");
				content.append(entityAttribute.getTname());
				content.append("?c}");
			}else if("boolean".equals(attributeType)){
				content.append("<a href='javascript:void(0)' id=\"${obj.id?default('')}\" onClick=\"updateBaseObject(this,${obj."+entityAttribute.getTname()+"?string('false','true')})\" name=" +entityAttribute.getTname()+ "  value= ${obj.");
				content.append(entityAttribute.getTname());
				content.append("?string('false','true')}> ${obj." +entityAttribute.getTname()+
						"?string('启用','禁用')}</a>");
			}else if("Date".equals(attributeType)){
				content.append("${obj.");
				content.append(entityAttribute.getTname());
				content.append("?string(\"yyyy-MM-dd\")}");
			}else if("double".equals(attributeType)){
				content.append("${obj.");
				content.append(entityAttribute.getTname());
				content.append("?string(\",##0.00\")}");
			}
			content.append("</td>\n");
		}
		content.append("</tr>\n");
		content.append("</#list>\n");
		content.append("</tbody>\n");
		content.append("</table>\n");
		content.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"tab\">\n");
		content.append("<tfoot>\n");
		content.append("<tr>\n");
		content.append("<td height=\"22\" align=\"right\" bgcolor=\"#ebeaf4\"><@pagination.pagination page=page />&nbsp;</td>\n");
		content.append("</tr>\n");
		content.append("</tfoot>\n");
		content.append("</table>\n");
		content.append("<script type=\"text/javascript\">\n");
		content.append("<!--\n");
		content.append("initSort();\n");
		content.append("//-->\n");
		content.append("</script>\n");
		content.append("<@common.bottom/>\n");
		FileUtil.WriteFile(filePath, content);
	}
	/***
	 * 生成新建页面
	 * @param filePath
	 * @param entity
	 * @throws Exception
	 */
	private void generateAddPage(String filePath, Appes entity) throws Exception {
		StringBuilder content=new StringBuilder();
		content.append("<#import \"../../macro/macrocommon.htm\" as common>\n");
		content.append("<@common.header/>\n");
		content.append("<@common.addheader name=\""+entity.getTname()+"\"/>\n");
		content.append("<form id=\"formId\" method=\"post\" action=\"?action=save\" >\n");
		content.append("<table cellSpacing=1 cellPadding=2 width=\"90%\" align=center bgColor=#cccccc border=0 class=\"tab\">\n");
		content.append("<thead>\n");
		content.append("<tr>\n");
		content.append("<td colspan=4>\n");
		content.append("新建"+entity.getTname());
		content.append("</td>\n");
		content.append("</tr>\n");
		content.append("</thead>\n");
		content.append("<tbody>\n");
		List list=appesAttributeDomain.queryByCriteria(appesAttributeDomain.getCriteria(null).add(Restrictions.eq("appes", entity.getId())).add(Restrictions.eq("addDisplay", true)));
		int size=list.size();
		for(int t=0;t<size;t++){
			AppesAttribute entityAttribute=(AppesAttribute) list.get(t);
			String displayType=entityAttribute.getDisplayType();
			String attributeType=entityAttribute.getAttributeType();
			boolean nullValue=entityAttribute.isNullValue();
			if(t%2==0){
				content.append("<tr>\n");
				content.append("<td class=\"titleTd\">");
				content.append(entityAttribute.getDescription());
				content.append("</td>\n");
				content.append("<td  class=\"contentTd\">");
				if("select".equals(displayType)){
					content.append("<select name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" >");
					if("valid".equals(entityAttribute.getTname())||attributeType.equals("boolean")){
						content.append("<@common.valid />");
					}else if("parentId".equals(entityAttribute.getTname())){
						content.append("<@common.options list=parentlist />");
					}else{
						content.append("<@common.options list=codelist />");
					}
					content.append("</select>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("textarea".equals(displayType)){
					content.append("<textarea style=\"width: 60%\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\"/></textarea>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("date".equals(displayType)){
					content.append("<input type=\"text\" required=true class=\"calendars\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\"/><span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("datetime".equals(displayType)){
					content.append("<input type=\"text\" required=true class=\"calendars\" param=\"ymdhms\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\"/><span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else{
					content.append("<input type=\"text\"  style=\"width: 60%\" name=\"");
					content.append(entityAttribute.getTname());
					if(!nullValue){
						content.append("\" class=\"btinput\" required=true ");
					}else{
						content.append("\" class=\"txtinput\"");
					}
					if(attributeType.equals("int")||attributeType.equals("Integer")){
						content.append(" check=\"isInt\"");
					}else if(attributeType.equals("double")){
						content.append(" check=\"isFloat('')\"");
					}
					content.append("/><span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}
				content.append("</td>\n");
			}else{
				content.append("<td class=\"titleTd\">");
				content.append(entityAttribute.getDescription());
				content.append("</td>\n");
				content.append("<td class=\"contentTd\">");
				if("select".equals(displayType)){
					content.append("<select  name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" >");
					if("valid".equals(entityAttribute.getTname())||attributeType.equals("boolean")){
						content.append("<@common.valid />");
					}else if("parentId".equals(entityAttribute.getTname())){
						content.append("<@common.options list=parentlist/>");
					}else{
						content.append("<@common.options list=codelist/>");
					}
					content.append("</select>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("textarea".equals(displayType)){
					content.append("<textarea style=\"width: 60%\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\"/></textarea>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("date".equals(displayType)){
					content.append("<input type=\"text\" required=true class=\"calendars\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\"/><span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("datetime".equals(displayType)){
					content.append("<input type=\"text\" required=true class=\"calendars\" param=\"ymdhms\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\"/><span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else{
					content.append("<input type=\"text\" style=\"width: 60%\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					if(!nullValue){
						content.append("\" class=\"btinput\" required=true ");
					}else{
						content.append("\" class=\"txtinput\"");
					}
					
					if(attributeType.equals("int")||attributeType.equals("Integer")){
						content.append(" check=\"isInt\"");
					}else if(attributeType.equals("double")){
						content.append(" check=\"isFloat('')\"");
					}
					content.append(" /><span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}
				content.append("</td>\n");
				content.append("</tr>\n");
			}
		}
		if(size%2==1){
			content.append("<td class=\"titleTd\">");
			content.append("</td>\n");
			content.append("<td  class=\"contentTd\">");
			content.append("</td>\n");
			content.append("</tr>\n");
		}
		content.append("<tr>\n");
		content.append("<td colspan=4 height=30 align=\"center\">\n");
		content.append("<input type=\"button\" class=\"buttons\" value=\"保存\" onclick=\"save()\" />&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"button\" class=\"buttons\" value=\"关闭\" onclick=\"javascript:history.go(-1);\" />\n");
		content.append("</td>\n");
		content.append("</tr>\n");
		content.append("</tbody>\n");
		content.append("</table>\n");
		content.append("</form>\n");
		content.append("<@common.bottom/>\n");
		FileUtil.WriteFile(filePath, content);
	}
	/***
	 * 生成只读页面
	 * @param filePath
	 * @param entity
	 * @throws Exception
	 */
	private void generateReadPage(String filePath, Appes entity) throws Exception {
		StringBuilder content=new StringBuilder();
		content.append("<#import \"../../macro/macrocommon.htm\" as common>\n");
		content.append("<@common.header/>\n");
		content.append("<@common.readheader name=\""+entity.getTname()+"\"/>\n");
		content.append("<form id=\"formId\" method=\"post\" action=\"?action=update\" >\n");
		content.append("<input type=\"hidden\" name=id value=\"${obj.id?default('')}\" />");
		content.append("<input type=\"hidden\" name=page value=\"${page?c}\"  />");
		content.append("<table cellSpacing=1 cellPadding=2 width=\"90%\" align=center bgColor=#cccccc border=0 class=\"tab\">\n");
		content.append("<thead>\n");
		content.append("<tr>\n");
		content.append("<td colspan=4>\n");
		content.append("查看"+entity.getTname());
		content.append("</td>\n");
		content.append("</tr>\n");
		content.append("</thead>\n");
		content.append("<tbody>\n");
		List list=appesAttributeDomain.queryByCriteria(appesAttributeDomain.getCriteria(null).add(Restrictions.eq("appes", entity.getId())).add(Restrictions.eq("editDisplay", true)));
		int size=list.size();
		for(int t=0;t<size;t++){
			AppesAttribute entityAttribute=(AppesAttribute) list.get(t);
			String displayType=entityAttribute.getDisplayType().toLowerCase();
			String attType=entityAttribute.getAttributeType();
			boolean nullValue=entityAttribute.isNullValue();
			if(t%2==0){
				content.append("<tr>\n");
				content.append("<td class=\"titleTd\">");
				content.append(entityAttribute.getDescription());
				content.append("</td>\n");
				content.append("<td class=\"contentTd\">");
				if("select".equals(displayType)){
					content.append("<select disabled=\"disabled\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" >");
					if("valid".equals(entityAttribute.getTname())||attType.equals("boolean")){
						content.append("<@common.valid defaultValue=\"${obj.valid?string('true','false')}\" />");
					}else if("parentId".equals(entityAttribute.getTname())){
						content.append("<@common.options list=parentlist defaultValue=\"${obj.parentId}\"/>");
					}else{
						content.append("<@common.options defaultValue=\"${obj.code}\" />");
					}
					content.append("</select>");
				}else if("textarea".equals(displayType)){
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}
					content.append("}");
				}else if("date".equals(displayType)){
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd\")}");
					}
					content.append("}");
				}else  if("datetime".equals(displayType)){
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd hh:mm\")}");
					}
					content.append("} \"");
				}else{
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}else if("Integer".equals(attType)){
						content.append("?c");
					}else if("int".equals(attType)){
						content.append("?c");
					}else if("double".equals(attType)){
						content.append("?string('')");
					}
					content.append("}\"");
				}
				content.append("</td>\n");
			}else{
				content.append("<td class=\"titleTd\">");
				content.append(entityAttribute.getDescription());
				content.append("</td>\n");
				content.append("<td class=\"contentTd\">");
				if("select".equals(displayType)){
					content.append("<select disabled=\"disabled\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" >");
					if("valid".equals(entityAttribute.getTname())||attType.equals("boolean")){
						content.append("<@common.valid defaultValue=\"${obj.valid?string('true','false')}\" />");
					}else if("parentId".equals(entityAttribute.getTname())){
						content.append("<@common.options list=parentlist defaultValue=\"${obj.parentId}\"/>");
					}else{
						if("Integer".equals(attType)){
							content.append("<@common.options />");
						}else{
							content.append("<@common.options defaultValue=\"${obj.code}\" />");
						}
					}
					content.append("</select>");
				}else if("textarea".equals(displayType)){
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}
					content.append("}");
				}else if("date".equals(displayType)){
					content.append("\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd\")}");
					}
					content.append("}\"");
				}else  if("datetime".equals(displayType)){
					content.append("\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd hh:mm\")}");
					}
					content.append("}\"");
				}else{
					content.append("${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}else if("Integer".equals(attType)){
						content.append("?c");
					}else if("int".equals(attType)){
						content.append("?c");
					}else if("double".equals(attType)){
						content.append("?string('')");
					}
					content.append("}\"");
				}
				content.append("</td>\n");
				content.append("</tr>\n");
			}
		}
		if(size%2==1){
			content.append("<td class=\"titleTd\">");
			content.append("</td>\n");
			content.append("<td class=\"contentTd\">");
			content.append("</td>\n");
			content.append("</tr>\n");
		}
		content.append("<tr>\n");
		content.append("<td colspan=4 height=30 align=\"center\">\n");
		content.append("<input type=\"button\" class=\"buttons\" value=\"编辑\" onclick=\"turnToEdit('${obj.id}')\" />&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"button\" class=\"buttons\" value=\"关闭\" onclick=\"javascript:history.go(-1);\" />\n");
		content.append("</td>\n");
		content.append("</tr>\n");
		content.append("</tbody>\n");
		content.append("</table>\n");
		content.append("</form>\n");
		content.append("<@common.bottom/>\n");
		FileUtil.WriteFile(filePath, content);
	}
	
	/***
	 * 生成编辑页面
	 * @param filePath
	 * @param entity
	 * @throws Exception
	 */
	private void generateEditPage(String filePath, Appes entity) throws Exception {
		StringBuilder content=new StringBuilder();
		content.append("<#import \"../../macro/macrocommon.htm\" as common>\n");
		content.append("<@common.header/>\n");
		content.append("<@common.editheader name=\""+entity.getTname()+"\"/>\n");
		content.append("<form id=\"formId\" method=\"post\" action=\"?action=update\" >\n");
		content.append("<input type=\"hidden\" name=id value=\"${obj.id?default('')}\" />");
		content.append("<input type=\"hidden\" name=page value=\"${page?c}\"  />");
		content.append("<table cellSpacing=1 cellPadding=2 width=\"90%\" align=center bgColor=#cccccc border=0 class=\"tab\">\n");
		content.append("<thead>\n");
		content.append("<tr>\n");
		content.append("<td colspan=4>\n");
		content.append("编辑"+entity.getTname());
		content.append("</td>\n");
		content.append("</tr>\n");
		content.append("</thead>\n");
		content.append("<tbody>\n");
		List list=appesAttributeDomain.queryByCriteria(appesAttributeDomain.getCriteria(null).add(Restrictions.eq("appes", entity.getId())).add(Restrictions.eq("editDisplay", true)));
		int size=list.size();
		for(int t=0;t<size;t++){
			AppesAttribute entityAttribute=(AppesAttribute) list.get(t);
			String displayType=entityAttribute.getDisplayType().toLowerCase();
			String attType=entityAttribute.getAttributeType();
			boolean nullValue=entityAttribute.isNullValue();
			if(t%2==0){
				content.append("<tr>\n");
				content.append("<td class=\"titleTd\">");
				content.append(entityAttribute.getDescription());
				content.append("</td>\n");
				content.append("<td class=\"contentTd\">");
				if("select".equals(displayType)){
					content.append("<select  name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" >");
					if("valid".equals(entityAttribute.getTname())||attType.equals("boolean")){
						content.append("<@common.valid defaultValue=\"${obj.valid?string('true','false')}\" />");
					}else if("parentId".equals(entityAttribute.getTname())){
						content.append("<@common.options list=parentlist defaultValue=\"${obj.parentId}\"/>");
					}else{
						content.append("<@common.options defaultValue=\"${obj.code}\" />");
					}
					content.append("</select>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("textarea".equals(displayType)){
					content.append("<textarea style=\"width: 60%\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" />${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}
					content.append("} </textarea>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("date".equals(displayType)){
					content.append("<input type=\"text\" class=\"calendars\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" value=\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd\")}");
					}
					content.append("}\" />");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else  if("datetime".equals(displayType)){
					content.append("<input type=\"text\"  class=\"calendars\" param=\"ymdhms\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" value=\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd hh:mm\")}");
					}
					content.append("} \" />");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else{
					content.append("<input type=\"text\" style=\"width: 60%\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					if(!nullValue){
						content.append("\" class=\"btinput\" required=true ");
					}else{
						content.append("\" class=\"txtinput\"");
					}
					if(attType.equals("int")||attType.equals("Integer")){
						content.append(" check=\"isInt\"");
					}else if(attType.equals("double")){
						content.append(" check=\"isFloat('')\"");
					}
					content.append(" value=\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}else if("Integer".equals(attType)){
						content.append("?c");
					}else if("int".equals(attType)){
						content.append("?c");
					}else if("double".equals(attType)){
						content.append("?string('')");
					}
					content.append("}\" />");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}
				content.append("</td>\n");
			}else{
				content.append("<td class=\"titleTd\">");
				content.append(entityAttribute.getDescription());
				content.append("</td>\n");
				content.append("<td bgColor=#ffffff>");
				if("select".equals(displayType)){
					content.append("<select  name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" >");
					if("valid".equals(entityAttribute.getTname())||attType.equals("boolean")){
						content.append("<@common.valid defaultValue=\"${obj.valid?string('true','false')}\" />");
					}else if("parentId".equals(entityAttribute.getTname())){
						content.append("<@common.options list=parentlist defaultValue=\"${obj.parentId}\"/>");
					}else{
						if("Integer".equals(attType)){
							content.append("<@common.options />");
						}else{
							content.append("<@common.options defaultValue=\"${obj.code}\" />");
						}
					}
					content.append("</select>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("textarea".equals(displayType)){
					content.append("<textarea name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" />${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}
					content.append("} </textarea>");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else if("date".equals(displayType)){
					content.append("<input type=\"text\" class=\"calendars\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" value=\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd\")}");
					}
					content.append("}\" />");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else  if("datetime".equals(displayType)){
					content.append("<input type=\"text\"  class=\"calendars\" param=\"ymdhms\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("\" value=\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?string(\"yyyy-MM-dd hh:mm\")}");
					}
					content.append("}\" />");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}else{
					content.append("<input type=\"text\" name=\"");
					content.append(entityAttribute.getTname());
					content.append("\" id=\"");
					content.append(entityAttribute.getTname());
					if(!nullValue){
						content.append("\" class=\"btinput\" required=true ");
					}else{
						content.append("\" class=\"txtinput\"");
					}
					if(attType.equals("int")||attType.equals("Integer")){
						content.append(" check=\"isInt\"");
					}else if(attType.equals("double")){
						content.append(" check=\"isFloat('')\"");
					}
					content.append(" value=\"${obj.");
					content.append(entityAttribute.getTname());
					if("String".equals(attType)){
						content.append("?default('')");
					}else if("Integer".equals(attType)){
						content.append("?c");
					}else if("int".equals(attType)){
						content.append("?c");
					}else if("double".equals(attType)){
						content.append("?string('')");
					}
					content.append("}\" />");
					content.append("<span class=\"spantip\" id=\"");
					content.append(entityAttribute.getTname());
					content.append("_span\"/>");
				}
				content.append("</td>\n");
				content.append("</tr>\n");
			}
		}
		if(size%2==1){
			content.append("<td class=\"titleTd\">");
			content.append("</td>\n");
			content.append("<td class=\"contentTd\">");
			content.append("</td>\n");
			content.append("</tr>\n");
		}
		content.append("<tr>\n");
		content.append("<td colspan=4 height=30 align=\"center\">\n");
		content.append("<input type=\"button\" class=\"buttons\" value=\"更新\" onclick=\"save()\" />&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"button\" class=\"buttons\" value=\"关闭\" onclick=\"javascript:history.go(-1);\" />\n");
		content.append("</td>\n");
		content.append("</tr>\n");
		content.append("</tbody>\n");
		content.append("</table>\n");
		content.append("</form>\n");
		content.append("<@common.bottom/>\n");
		FileUtil.WriteFile(filePath, content);
	}
	/***
	 * 初始化操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void initAction(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			DetachedCriteria criteria = baseDomain.getCriteria(null);
			criteria.add(Restrictions.eq("subSystemName", this.getSubSystem()));
			criteria.addOrder(Order.desc("id"));
			List list= baseDomain.queryByCriteria(criteria);
			int listsize=list.size();
			for(int t=0;t<listsize;t++){
				Appes appes=(Appes)list.get(t);
				List saveList=new ArrayList();
				if(appes.getControllerClassName()!=null&&!appes.getControllerClassName().equals("")&&appes.getActionList().size()==0){
					Object obj=null;
					try{
						if(appes!=null&&appes.getControllerClassName()!=null&&!appes.getControllerClassName().equals("")){
							obj=Class.forName(appes.getControllerClassName()).newInstance();
						}
					}catch(Exception e){
						logger.info("============控制器不存在========="+appes.getControllerClassName());
					}
					
					if(obj==null&&this.getApplicationContext().containsBean(appes.getId()+"Controller")){
						obj=this.getApplicationContext().getBean(appes.getId()+"Controller");
					}
					if(obj!=null&&obj instanceof BaseAjaxController){// 如果是默认系统级别控制器
						Method[] methods=obj.getClass().getDeclaredMethods();
						Map templeMAap=new HashMap();
						templeMAap.put("save", "保存操作_form");
						templeMAap.put("list", "列表操作_list");
						templeMAap.put("add", "新增操作_list");
						templeMAap.put("update", "更新操作_form");
						templeMAap.put("edit", "编辑操作_list");
						templeMAap.put("importdatas", "数据导入操作_list");
						templeMAap.put("jsdelete", "标记删除操作_list");
						templeMAap.put("delete", "删除操作_list");
						templeMAap.put("updateBaseObject", "更改状态操作_list");
						int sortCode=1;
						for(Object o : templeMAap.keySet()){
							String actionName=o.toString();
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
						}
					}else{//自定义控制器
						System.out.println("-------------obj.getClass().getName------------");
					}
				}
			}
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
	}/***
	 * 公共的选择列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView commonselectlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String objectId=HttpUtil.getString(request, "objectId","");
		String objectName=HttpUtil.getString(request, "objectName","");
		String tname=HttpUtil.getString(request, "tname","");
		String selectType=HttpUtil.getString(request, "selectType","");
		String sortfield =HttpUtil.getString(request, "sortfield","");
		String sortvalue =HttpUtil.getString(request, "sortvalue","");
		List list=new ArrayList();
		Page page= HttpUtil.convertPage(request);
		String id=HttpUtil.getString(request, "id", "");
		Appes entity=(Appes) baseDomain.query(id);
		DetachedCriteria criteria = DetachedCriteria.forClass(Class.forName(entity.getClassName()));
		if(!tname.equals("")){
			criteria.add(Restrictions.ilike("tname", "%"+tname+"%"));
		}
		if(!sortfield.equals("")&&!sortvalue.equals("")){
			boolean sortValue=HttpUtil.getBoolean(request, "sortvalue", true);
			if(sortValue){
				criteria.addOrder(Order.desc(sortfield));
			}else{
				criteria.addOrder(Order.asc(sortfield));
			}
		}else{
			criteria.addOrder(Order.desc("id"));
		}
		list=baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);		
		model.put("page", page);		
		model.put("sortfield", sortfield);		
		model.put("sortvalue", sortvalue);		
		model.put("objectId", objectId);		
		model.put("objectName", objectName);		
		model.put("tname", tname);		
		model.put("selectType", selectType);		
		return new ModelAndView(CommonSelectPage,model);
	}
	/***
	 * 自动完成数据获取列别
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void autocomplete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String  id=HttpUtil.getString(request, "id", "");
		String  queryCode=HttpUtil.getString(request, "queryCode", "");
		int  maxSize=HttpUtil.getInt(request, "maxSize", 10);
		if(!id.equals("")&&!queryCode.equals("")){
			Appes appes=(Appes)this.baseDomain.query(id);
			if(appes!=null){
				String tableName=appes.getTableName();
				DetachedCriteria criteria = DetachedCriteria.forClass(Class.forName(appes.getClassName()));
				criteria.add(Restrictions.or(Restrictions.like("id", queryCode, MatchMode.ANYWHERE), Restrictions.like("tname", queryCode, MatchMode.ANYWHERE)));
//				List<BaseObject> list=this.baseDomain.queryByCriteria(criteria, maxSize);
				String sql="select id,tname from "+tableName+" where id like '%"+queryCode+"%'";
				List<IdName> list=jdbcTemplate.query(sql, new IDNAMEMapper());
				JSONArray jsonArray = new JSONArray();
				for(int t=0;t<list.size();t++){
					jsonArray.add(JSONObject.fromObject(list.get(t)));
				}
				this.outJsonObject(response,list.get(0));
				
			}
		}
	}
	public void setMenuDomain(MenuDomain menuDomain) {
		this.menuDomain = menuDomain;
	}
	public String getSubSystem() {
		return subSystem;
	}
	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}
	public void setAppesAttributeDomain(AppesAttributeDomain appesAttributeDomain) {
		this.appesAttributeDomain = appesAttributeDomain;
	}
	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
