package com.starsoft.core.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.core.domain.ResourceDomain;
import com.starsoft.core.domain.SystemPropertyDomain;
import com.starsoft.core.entity.Resource;
import com.starsoft.core.entity.SystemProperty;
import com.starsoft.core.task.FileConvertTask;
import com.starsoft.core.util.FileUtil;
import com.starsoft.core.util.FlashUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.vo.FileUpload;

/***
 * 文件上传下载
 * @author lenovo
 *
 */
public class FileUploadNewController extends BaseAjaxController {
	public static final String FileUploadNewPage="common/uploadNewPage";
	public static final String SuccessPage="common/javascriptNewPage";
	private ResourceDomain resourceDomain;
	private SystemPropertyDomain systemPropertyDomain;
	private Map<String,String> typeMap=new HashMap<String,String>();
	@Autowired
	private FileConvertTask fileConverTask;
	/**
	 * 文件上传操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView fileUplod(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String baseObjectId=HttpUtil.getString(request, "baseObjectId", "");
		initMap();
		String uploadPath =this.createFolder();
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("doAction", "returnmsg();");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS"); 
		String fileNameResult="/systemInfo/upload/"+sdf.format(new Date()).substring(0,6)+"/";
		try{
			FileUpload entity=new FileUpload();
			this.bind(request, entity);
			MultipartFile file=entity.getFile();
			if(file!=null&&file.getSize()>0){
				String filename = file.getOriginalFilename();
				String currentFileType = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
				if(typeMap.get(entity.getFileType()).indexOf(currentFileType)==-1){//判断类型
					model.put("err", "上传失败！文件类型不允许！仅能上传类型："+typeMap.get(entity.getFileType()));
					model.put("msg", fileNameResult);
					return new ModelAndView(SuccessPage,model);
				}
				if(this.getFileSize()<file.getSize()){//判断大小
					model.put("err", "上传失败！文件大小超过了限制！最大上传限制为："+gerFileSizeMb()+"MB");
					model.put("msg", fileNameResult);
					return new ModelAndView(SuccessPage,model);
				}
				String filelinkname = sdf.format(new Date())+filename.substring(filename.lastIndexOf("."));   
	            BufferedInputStream in = new BufferedInputStream(file.getInputStream());  
	            if(entity.getFileType().equals("report")){//如果是报表
	            	String reportsPath = getServletContext().getRealPath("/")+ "reports\\";//获取文件路径 
	            	File repfile=new File(reportsPath + "\\" + filename);
	            	if(filename.getBytes().length!=filename.length()){
	            		model.put("err", "上传失败！文件名称不能包含中文！");
	        			model.put("msg", fileNameResult); 
	        			return new ModelAndView(SuccessPage,model);
//	            	}else if(repfile.exists()){
//	            		model.put("err", "上传失败！文件已经存在");
//	        			model.put("msg", "reportsPath/" + filename);
//	        			return new ModelAndView(SuccessPage,model);
	            	}else{
	            		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(repfile));   
		            	Streams.copy(in, out, true); // 开始把文件写到你指定的上传文件夹 
		            	fileNameResult="/reports/" + filename;
	            	}
	            }else if(typeMap.get("documentType").indexOf(currentFileType) != -1){//文档类的需要转换
	            	BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(uploadPath + "\\" + filelinkname)));   
	            	Streams.copy(in, out, true); // 开始把文件写到你指定的上传文件夹
	            	fileConverTask.fileConverter(HttpUtil.getLoginUser(request).getId(), baseObjectId, 
	            			fileNameResult, uploadPath, filename, filelinkname, currentFileType);//执行文件转换，异步
	            	fileNameResult = fileNameResult + filelinkname;
	            }else {//图片类的
	            	BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(uploadPath + "\\" + filelinkname)));   
	            	Streams.copy(in, out, true); // 开始把文件写到你指定的上传文件夹
	            	fileNameResult=fileNameResult+filelinkname;
	 		        Resource resource=(Resource)resourceDomain.getBaseObject();
	 		    	String resourceName=filename;
	 				if(filename.indexOf(".")>0){
	 					resourceName=filename.substring(0, filename.indexOf("."));
	 				}
	 		        resource.setTname(resourceName);
	 		        resource.setFileType(entity.getFileType());
	 		        resource.setValid(true);
	 		        resource.setUrl(fileNameResult);
	 		        resource.setBaseObjectId(baseObjectId);
	 		        this.saveBaseInfoToObject(request, resource);
	 		        resourceDomain.save(resource);
				}
		       
			}
		}catch(Exception e){
			e.getMessage();
			model.put("err", "上传失败！");
			model.put("msg", fileNameResult);
		}
		String returnId=HttpUtil.getString(request, "returnId", "");
		String returnName=HttpUtil.getString(request, "returnName", "");
		model.put("returnId",returnId);
		model.put("returnName",returnName);
		model.put("err", "");
		model.put("msg", fileNameResult);
//		return new ModelAndView("redirect:http://www.2edus.com/returnfileupdateresult.html?returnId="+returnId+"&filename="+fileNameResult);
		return new ModelAndView(SuccessPage,model);
	}
	/**
	 * js文件上传操作fu
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void jsfileUplod(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String uploadPath =this.createFolder();
		JSONObject main = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS"); 
		String fileNameResult="/systemInfo/upload/"+sdf.format(new Date()).substring(0,6)+"/";
	    String contentType = request.getContentType();
		if (ServletFileUpload.isMultipartContent(request)) {
			DefaultMultipartHttpServletRequest httprequest = (DefaultMultipartHttpServletRequest) request;
			MultipartFile file = httprequest.getFile("filedata");
			String filename = file.getOriginalFilename();
			if(this.getFileSize()<file.getSize()){//判断大小
				main.put("err", "上传失败！文件大小超过了限制！最大上传限制为："+gerFileSizeMb()+"MB");
				main.put("msg", fileNameResult);
			}else if(getAllTypes().indexOf(filename.substring(filename.lastIndexOf(".")+1).toLowerCase())==-1){//判断类型
				main.put("err", "上传失败！文件类型不允许！仅能上传类型："+getAllTypes());
				main.put("msg", fileNameResult);
			}else{
				String filelinkname = sdf.format(new Date())
						+ filename.substring(filename.lastIndexOf("."));
				BufferedInputStream in = new BufferedInputStream(file
						.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File(uploadPath + "\\"
								+ filelinkname)));
				Streams.copy(in, out, true); // 开始把文件写到你指定的上传文件夹
				fileNameResult += filelinkname;
				Resource resource=(Resource)resourceDomain.getBaseObject();
				String resourceName=filename;
				if(filename.indexOf(".")>0){
					resourceName=filename.substring(0, filename.indexOf("."));
				}
		        resource.setTname(resourceName);
		        String fileType=file.getContentType();
		        if(fileType.indexOf("/")>-1){
		        	fileType=fileType.substring(0, fileType.indexOf("/"));
		        }
		        resource.setFileType(fileType);
		        resource.setValid(true);
		        resource.setUrl(fileNameResult);
		        this.saveBaseInfoToObject(request, resource);
		        resourceDomain.save(resource);
				main.put("err", "");
				main.put("msg", fileNameResult);
			}
		}else if("application/octet-stream".equals(contentType)){// html5 上传
			try {
                String dispoString = request.getHeader("Content-Disposition");
                int iFindStart = dispoString.indexOf("name=\"")+6;
                int iFindEnd = dispoString.indexOf("\"", iFindStart);
                iFindStart = dispoString.indexOf("filename=\"")+10;
                iFindEnd = dispoString.indexOf("\"", iFindStart);
                String sFileName = dispoString.substring(iFindStart, iFindEnd);
                sFileName=URLDecoder.decode(sFileName, "UTF-8");
                String fileType=sFileName.substring(sFileName.lastIndexOf(".")+1);
                int filesize = request.getContentLength();
                if (filesize > this.getFileSize()) { //检查文件大小
                	main.put("err", "上传失败！文件大小超过了限制！最大上传限制为："+gerFileSizeMb()+"MB");
    				main.put("msg", fileNameResult);
                }else if(getAllTypes().indexOf(fileType)==-1){//
                	main.put("err", "上传失败！文件类型不允许！仅能上传类型："+getAllTypes());
    				main.put("msg", fileNameResult);
                }else{
                	String filelinkname = sdf.format(new Date())+ sFileName.substring(sFileName.lastIndexOf("."));
                    BufferedInputStream in = new BufferedInputStream(request.getInputStream());
    				BufferedOutputStream out = new BufferedOutputStream(
    						new FileOutputStream(new File(uploadPath + "\\"
    								+ filelinkname)));
    				Streams.copy(in, out, true); // 开始把文件写到你指定的上传文件夹
    				fileNameResult += filelinkname;
    				Resource resource=(Resource)resourceDomain.getBaseObject();
    				String resourceName=sFileName;
    				if(sFileName.indexOf(".")>0){
    					resourceName=sFileName.substring(0, sFileName.indexOf("."));
    				}
    		        resource.setTname(resourceName);
    		        resource.setFileType(fileType);
    		        resource.setValid(true);
    		        resource.setUrl(fileNameResult);
    		        this.saveBaseInfoToObject(request, resource);
    		        resourceDomain.save(resource);
    		        main.put("err", "");
    				main.put("msg", fileNameResult);
                }
            } catch (Exception ex) {
                main.put("err", "上传失败！");
    			main.put("msg", fileNameResult);
            }
		}else {
			main.put("err", "上传失败！");
			main.put("msg", fileNameResult);
		}
		this.outJsonString(response, main.toString());
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
		initMap();
		Map<String,Object> model = new HashMap<String,Object>();
		String fileType = HttpUtil.getString(request, "fileType", "all");
		String baseObjectId = HttpUtil.getString(request, "baseObjectId", "");
		String returnId=HttpUtil.getString(request, "returnId", "");
		String returnName=HttpUtil.getString(request, "returnName", "");
		model.put("returnId",returnId);
		model.put("returnName",returnName);
		model.put("fileType",fileType);
		model.put("baseObjectId",baseObjectId);
		model.put("fileTypes",typeMap.containsKey(fileType)?typeMap.get(fileType):"");
		return new ModelAndView(this.FileUploadNewPage,model);
	}
	public String createFolder(){
		String uploadPath = "D:\\systemInfo\\";//要发布的系统 
		if(!(new File(uploadPath).isDirectory())){
			new File(uploadPath).mkdir();
		}
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
	/***
	 * 文件下载
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void filedown(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String id=HttpUtil.getString(request, "id", "");
		if(!id.equals("")){
			Resource resource=(Resource)this.resourceDomain.query(id);
			if(resource!=null){
				String filePath=resource.getUrl();
				File t_file = new java.io.File(filePath);
				long l = t_file.length(); //文件长度
				FileInputStream in = null;
				String fileName=resource.getTname();
				try {
					  in = new FileInputStream(t_file);
					  if (in != null) {
				            String fs = t_file.getName();
				            response.reset();
				            int st=fs.lastIndexOf(".");
				            String filesuffix=fs.substring(st,fs.length());
				            if(fs.endsWith(".tif")) {
				            	response.setContentType("image/tiff"); //
				            } else {
				            	response.setContentType("application/x-msdownload;charset=GBK"); //
				            }
				            response.setHeader("Content-Disposition", "attachment; filename=" + FileUtil.encodingFileName(fileName)+filesuffix);
				            response.setContentLength( (int) l); //设置输入文件长度
				            byte[] b = new byte[11024];
				            int len = 0;
				            OutputStream outFile=response.getOutputStream();
				            while ( (len = in.read(b)) != -1) {
				              outFile.write(b, 0, len); //向浏览器输出
				            }
				            outFile.flush();
				            outFile.close();
				            in.close(); //关闭文件输入流
					  }
					}catch (Exception e) {
						this.outFailString(request,response, "读取文件出错，无法正常下载","");
					}
				
			}else{
				this.outFailString(request,response, "数据库中已经无文件记录，无法正常下载","");
			}
		}else{
			this.outFailString(request,response, "文件标识为空，无法正常下载","");
		}
		
	}
	private String getAllTypes(){
		String result=getDocumentType();
		result+=","+this.getFlashTypes();
		result+=","+this.getImageTypes();
		result+=","+this.getMediaTypes();
		return result;
	}
	private String getMediaTypes() {
		SystemProperty systemProperty=(SystemProperty)systemPropertyDomain.query("system.mediaTypes");
		if(systemProperty!=null){
			return systemProperty.getTname();
		}else{
			return "";
		}
	}
	private String getImageTypes() {
		SystemProperty systemProperty=(SystemProperty)systemPropertyDomain.query("system.imageTypes");
		if(systemProperty!=null){
			return systemProperty.getTname();
		}else{
			return "";
		}
	}
	private String getFlashTypes() {
		SystemProperty systemProperty=(SystemProperty)systemPropertyDomain.query("system.flashTypes");
		if(systemProperty!=null){
			return systemProperty.getTname();
		}else{
			return "";
		}
	}
	private void initMap(){
		typeMap.put("all", getAllTypes());
		typeMap.put("flash", getFlashTypes());
		typeMap.put("image", getImageTypes());
		typeMap.put("media", getMediaTypes());
		typeMap.put("report", "rptdesign");
		typeMap.put("documentType", getDocumentType());
	}
	private String getDocumentType() {
		SystemProperty systemProperty=(SystemProperty)systemPropertyDomain.query("system.documentType");
		if(systemProperty!=null){
			return systemProperty.getTname();
		}else{
			return "";
		}
	}
	public Integer getFileSize() {
		SystemProperty systemProperty=(SystemProperty)systemPropertyDomain.query("system.fileUpload.fileSize");
		if(systemProperty!=null){
			return Integer.valueOf(systemProperty.getTname());
		}else{
			return 20971520;
		}
	}
	public String gerFileSizeMb(){
		DecimalFormat decimalFormat =new DecimalFormat("0.00");
		Double result=(getFileSize()/1024.00)/1024.00;
		return decimalFormat.format(result);
	}
	public Map<String, String> getTypeMap() {
		return typeMap;
	}
	public void setTypeMap(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}
	public void setResourceDomain(ResourceDomain resourceDomain) {
		this.resourceDomain = resourceDomain;
	}
	public void setSystemPropertyDomain(SystemPropertyDomain systemPropertyDomain) {
		this.systemPropertyDomain = systemPropertyDomain;
	}

}
