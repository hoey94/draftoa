package com.starsoft.core.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.AppesAttribute;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.util.BeanUtil;
import com.starsoft.core.util.DateUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.FileUpload;

/**
 * 系统集成数据导入导出
 * @author Administrator
 *
 */
public class SystemIntegrationController extends BaseAjaxController {
	public static final String FileUploadPage="common/uploadPage";
	private Map<String,String> typeMap=new HashMap<String,String>();
	private JdbcTemplate jdbcTemplate;
	private AppesDomain appesDomain;
	/**
	 * 选择要导入数据列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView importlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("importlist"),model);
	}
	
	public ModelAndView importpage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		String app=HttpUtil.getString(request, "app", "");
		DetachedCriteria criteria=DetachedCriteria.forClass(AppesAttribute.class);
		Appes appes=(Appes)this.appesDomain.query(app);
		criteria.add(Restrictions.eq("appes", app));	
		List<AppesAttribute> list=this.baseDomain.queryByCriteria(criteria.addOrder(Order.asc("sortCode")));
		model.put("list", list);
		model.put("appes", appes);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("importpage"),model);
	}
	/***
	 * 导出数据模版
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void importtemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String app=HttpUtil.getString(request, "app", "");
		HSSFWorkbook workbook=new HSSFWorkbook(); 
		boolean hasdata=false;//是否有数据;
		Page page=new Page(0, 2);
		if(!app.equals("")){
			Appes appe=(Appes)this.appesDomain.query(app);
			if(appe==null) this.outFailString(request,response, "系统不存在要导出的数据！","");
			DetachedCriteria criteria=DetachedCriteria.forClass(AppesAttribute.class);
			criteria.add(Restrictions.eq("valid", true));
			criteria.add(Restrictions.eq("appes", app));			
			criteria.add(Restrictions.ne("tname", ""));			
			List<AppesAttribute> listColumns=this.baseDomain.queryByCriteria(criteria.addOrder(Order.asc("sortCode")));
			int columnsize=listColumns.size();
			if(columnsize<1) this.outFailString(request,response, "系统不存在要导出的数据！","");
			List datalist=this.baseDomain.queryByCriteria(DetachedCriteria.forClass(Class.forName(appe.getClassName())), page);
			HSSFSheet sheet = workbook.createSheet(appe.getId());    
	        HSSFRow sheetHeadRow = sheet.createRow(0); 
	        HSSFRow sheetHeadRowTwo = sheet.createRow(1);
	        List<String> columnname=new ArrayList();
	        List<String> columnType=new ArrayList();
	        hasdata=true;
	        for(int t=0;t<columnsize;t++){
	        	AppesAttribute appesAttribute=listColumns.get(t);
	        	String name=appesAttribute.getTname();
	        	String description=appesAttribute.getDescription();
		        sheetHeadRow.createCell(t).setCellValue("".equals(description)?name:description);
		        columnname.add(name);
		        columnType.add(appesAttribute.getAttributeType());
	        } 
	        
	        for(int t=0;t<columnsize;t++){
	        	AppesAttribute appesAttribute=listColumns.get(t);
	        	String name=appesAttribute.getTname();
	        	sheetHeadRowTwo.createCell(t).setCellValue(name);
	        }
	        for(int t=0;t<datalist.size();t++){
	        	HSSFRow sheetHeadRowTemp = sheet.createRow(t+2);
	        	BaseObject obj = (BaseObject)datalist.get(t);
	        	for(int s=0;s<columnsize;s++){
	        		String value="";
	        		String fieldname=columnname.get(s);
	        		String fileType=columnType.get(s);
	        		if(fileType.indexOf("starsoft")>-1){//自己定义的类对象
	        			value=BeanUtil.getProperty(obj, fieldname);
	        		}else{
	        			value=BeanUtils.getProperty(obj, fieldname);
	        		}
	        		sheetHeadRowTemp.createCell(s).setCellValue(value); 
	        	}
	        }
		}
		if(hasdata){//说明有待导出的数据
			 String filename = app+".xls";//设置下载时客户端Excel的名称     
	         response.setContentType("application/vnd.ms-excel");     
	         response.setHeader("Content-disposition", "attachment;filename=" + filename);     
	         OutputStream ouputStream = response.getOutputStream();     
	         workbook.write(ouputStream);     
	         ouputStream.flush();     
	         ouputStream.close();
		}else{
			this.outFailString(request,response, "系统不存在要导出的数据！","");
		}
	}
	/***
	 * 导入数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void importdata(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			String app=HttpUtil.getString(request, "app", "");
			String idcheck=HttpUtil.getString(request, "idcheck", "");
			String namecheck=HttpUtil.getString(request, "namecheck", "");
			FileUpload entity=new FileUpload();
			this.bind(request, entity);
			MultipartFile file=entity.getFile();
			int successsize=0;
			String msg="";
			if(file!=null&&!file.isEmpty()){
				Workbook book = Workbook.getWorkbook(file.getInputStream());
				int sheetsize=book.getNumberOfSheets();
				if(sheetsize>0){
					Appes appe=(Appes)this.appesDomain.query(app);
					List<AppesAttribute> listColumns=new ArrayList();
					int listColumnsize=0;
					Map<String,String> attribtype=new HashMap<String,String>();
					if(appe!=null){
						DetachedCriteria criteria=DetachedCriteria.forClass(AppesAttribute.class);
						criteria.add(Restrictions.eq("valid", true));
						criteria.add(Restrictions.eq("appes", app));			
						criteria.add(Restrictions.ne("tname", ""));
						listColumns=this.baseDomain.queryByCriteria(criteria.addOrder(Order.desc("sortCode")));
						listColumnsize=listColumns.size();
						for(AppesAttribute appesAttribute:listColumns){
							attribtype.put(appesAttribute.getTname(), appesAttribute.getAttributeType());
						}						
					}
					for(int t=0;t<sheetsize;t++){
						Sheet sh = book.getSheet(t);
						int rows = sh.getRows();
						List<BaseObject> savelist=new ArrayList<BaseObject>();
						for (int i = 2; i < rows; i++) {
							BaseObject obj=(BaseObject) Class.forName(appe.getClassName()).newInstance();
							this.saveBaseInfoToObject(request, obj);
							Map<String,String> objvalue=new HashMap<String,String>();
							boolean savedata=true;// 需要保存该数据
							for(int s=0;s<listColumnsize;s++){
								String attribname = sh.getCell(s, 1).getContents().trim();
								String attribvalue = sh.getCell(s, i).getContents().trim();
								if(idcheck.equals("automatic")&&attribname.equals("id")){
									continue;
								}else if(idcheck.equals("contain")&&attribname.equals("id")&&!attribvalue.equals("")){//需要检查Id是否可以使用
									Object temp=this.appesDomain.query(Class.forName(appe.getClassName()), attribvalue);
									if(temp!=null){
										savedata=false;
										break;
									}
								}
								if(namecheck.equals("uniqueness")&&attribname.equals("tname")&&attribvalue.equals("")){//需要检查name是否可以使用
									savedata=false;
									break;
								}
								if(namecheck.equals("uniqueness")&&attribname.equals("tname")&&!attribvalue.equals("")){//需要检查name是否可以使用
									Object temp=this.baseDomain.queryFirstByProperty(Class.forName(appe.getClassName()), "tname", attribvalue);
									if(temp!=null){
										savedata=false;
										break;
									}
								}
								if(!attribname.equals("")&&!attribvalue.equals("")){
									objvalue.put(attribname, attribvalue);
								}
							}
							Method[] methods=Class.forName(appe.getClassName()).getMethods();
							for(Method method:methods){
								if(method.getName().substring(0,3).startsWith("set")){
									String attribNameTemp=method.getName().substring(3,4);
									String attribName=attribNameTemp.toLowerCase()+method.getName().substring(4);
									if(objvalue.containsKey(attribName)){
										method.invoke(obj, getObjectValue(attribtype.get(attribName),objvalue.get(attribName)));
									}
								}
							}
							if(savedata){
								successsize++;
								if(obj instanceof BaseTreeObject){//如果树形结构具有层级关系，逐个保存
									this.baseDomain.save(obj);
								}else{
									savelist.add(obj);
									if(successsize>500){
										this.baseDomain.save(savelist);
										savelist=new ArrayList();
									}
								}
							}
						}
						this.baseDomain.save(savelist);
					}
				}else{
					msg="请上传excel文件！";
					this.outFailString(request,response, msg,"");
				}
				
			}else{
				msg="请上传excel文件！";
				this.outFailString(request,response, msg,"");
			}
			this.outSuccessString(request,response,WEBCONSTANTS.SUCCESSINFOR);
	}
	public Object getObjectValue(String atttype,String value){
		if("java.lang.String".equals(atttype)||"String".equals(atttype)){
			return value;
		}else if("java.util.Date".equals(atttype)){
			return DateUtil.parseDateTime(value);
		}else if("int".equals(atttype)||"Integer".equals(atttype)||"java.lang.Integer".equals(atttype)){
			return Integer.valueOf(value);
		}else if("boolean".equals(atttype)||"Boolean".equals(atttype)){
			boolean result=false;
			if("true".equals(value)||"是".equals(value)||"Y".equals(value)||"Y".equals(value)||"正确".equals(value)||"启用".equals(value)){
				result=true;
			}
			return result;			
		}else if("double".equals(atttype)){
			return Double.valueOf(value);
		}
		return value;
	}
	/**
	 * 可以导出数据列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportlist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("exportlist"),model);
	}
	/**
	 * 导出所有数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void exportall(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<String> appes=HttpUtil.getList(request, "app", ";");
		HSSFWorkbook workbook=new HSSFWorkbook(); 
		boolean hasdata=false;//是否有数据;
		for(String app:appes){
			Appes appe=(Appes)this.appesDomain.query(app);
			if(appe==null) continue;
			DetachedCriteria criteria=DetachedCriteria.forClass(AppesAttribute.class);
			criteria.add(Restrictions.eq("valid", true));
			criteria.add(Restrictions.eq("appes", app));			
			criteria.add(Restrictions.ne("tname", ""));			
			List<AppesAttribute> listColumns=this.baseDomain.queryByCriteria(criteria.addOrder(Order.asc("sortCode")));
			int columnsize=listColumns.size();
			if(columnsize<1) continue;
			List datalist=this.baseDomain.queryByCriteria(DetachedCriteria.forClass(Class.forName(appe.getClassName())).add(Restrictions.eq("valid", true)));
			HSSFSheet sheet = workbook.createSheet(appe.getTname());    
	        HSSFRow sheetHeadRow = sheet.createRow(0); 
	        List<String> columnname=new ArrayList();
	        List<String> columnType=new ArrayList();
	        hasdata=true;
	        for(int t=0;t<columnsize;t++){
	        	AppesAttribute appesAttribute=listColumns.get(t);
	        	String name=appesAttribute.getTname();
	        	String description=appesAttribute.getDescription();
		        sheetHeadRow.createCell(t).setCellValue("".equals(description)?name:description);
		        columnname.add(name);
		        columnType.add(appesAttribute.getAttributeType());
	        }  
	        for(int t=0;t<datalist.size();t++){
	        	HSSFRow sheetHeadRowTemp = sheet.createRow(t+1);
	        	BaseObject obj = (BaseObject)datalist.get(t);
	        	for(int s=0;s<columnsize;s++){
	        		String value="";
	        		String fieldname=columnname.get(s);
	        		String fileType=columnType.get(s);
	        		if(fileType.indexOf("starsoft")>-1){//自己定义的类对象
	        			value=BeanUtil.getProperty(obj, fieldname);
	        		}else{
	        			value=BeanUtils.getProperty(obj, fieldname);
	        		}
	        		sheetHeadRowTemp.createCell(s).setCellValue(value); 
	        	}
	        }
		}
		if(hasdata){//说明有待导出的数据
			 String filename = "data.xls";//设置下载时客户端Excel的名称     
	         response.setContentType("application/vnd.ms-excel");     
	         response.setHeader("Content-disposition", "attachment;filename=" + filename);     
	         OutputStream ouputStream = response.getOutputStream();     
	         workbook.write(ouputStream);     
	         ouputStream.flush();     
	         ouputStream.close();
		}else{
			this.outFailString(request,response, "系统不存在要导出的数据！","");
		}
           
	}
	/**
	 * AJAX 公用方法
	 * @param response
	 * @param json
	 */
	public void outJsonString(HttpServletResponse response,String json) {  
		response.setContentType("text/html;charset=utf-8");   
        try {   
            PrintWriter out = response.getWriter();   
            out.write(json); 
            out.close();
        } catch (IOException e) {   
            e.printStackTrace();   
        }      
    }	
	
	/***
	 * 公共的转向上传文件页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String fileType = HttpUtil.getString(request, "fileType", "all");
		model.put("fileType",fileType);
		model.put("fileTypes",typeMap.containsKey(fileType)?typeMap.get(fileType):"");
		return new ModelAndView(this.FileUploadPage,model);
	}
	public String createFolder(){
		String uploadPath = getServletContext().getRealPath("/");//获取文件路径 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		uploadPath = uploadPath + "upload\\"; // 选定上传的目录此处为当前目录   
		if(!(new File(uploadPath).isDirectory())){
			new File(uploadPath).mkdir();
			uploadPath = uploadPath + sdf.format(new Date()).substring(0,6)+"\\"; // 选定上传的目录此处为当前目录
			new File(uploadPath).mkdir();
		}else{
			uploadPath = uploadPath + sdf.format(new Date()).substring(0,6)+"\\"; // 选定上传的目录此处为当前目录  
			if(!(new File(uploadPath).isDirectory())){
				new File(uploadPath).mkdir();
			}
		}
		return uploadPath;
	}
	public void setTypeMap(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}
	/**
	 * 数据修复表单页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView reparform(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String sql=HttpUtil.getString(request, "sql", "");
		if(!sql.equals("")){
			this.jdbcTemplate.execute(sql);
			model.put("msg", "执行成功！");
		}
		model.put("sql", sql);
		return new ModelAndView(this.getCustomPage("reparform"),model);
	}
	/**
	 * 展示表数据页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showdata(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String id=HttpUtil.getString(request, "id", "");
		Page page=HttpUtil.convertPage(request);
		List datalist=new ArrayList();
		if(!id.equals("")){
			Appes appes=appesDomain.getAppes(id);
			DetachedCriteria criteria = DetachedCriteria.forClass(Class.forName(appes.getClassName()));
			String name=HttpUtil.getString(request, "tname","");
			if(!name.equals("")){
				criteria.add(Restrictions.ilike("tname", "%"+name+"%"));
			}
			String sortfield =HttpUtil.getString(request, "sortfield","id");
			boolean sortValue=HttpUtil.getBoolean(request, "sortvalue", true);
			if(sortValue){
				criteria.addOrder(Order.desc(sortfield));
			}else{
				criteria.addOrder(Order.asc(sortfield));
			}
			datalist=this.baseDomain.queryByCriteria(criteria,page);
			model.put("appes", appes);
			model.put("tname", name);
		}
		model.put("list", datalist);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("showdata"),model);
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setAppesDomain(AppesDomain appesDomain) {
		this.appesDomain = appesDomain;
	}
		
}
