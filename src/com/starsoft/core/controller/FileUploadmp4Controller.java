 package com.starsoft.core.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.task.FileConvertTask;

/***
 * 文件上传下载
 * @author lenovo
 *
 */
public class FileUploadmp4Controller extends BaseAjaxController {
	public static final String FileUploadNewPage="common/uploadNewPageMp4";
	public static final String SuccessPage="common/javascriptNewPage";
	@Autowired
	private FileConvertTask fileConverTask;


	//监听文件进度
	public void fileUplodGO(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		HttpSession session =request.getSession();
		Integer plan = (Integer) session.getAttribute("fileuploadgo");
		if(plan==null)
		{
			plan = 0;
		}
		this.outSuccessString(request, response,String.valueOf(plan));
	}


	public ModelAndView uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		HttpSession session = request.getSession();
		session.setAttribute("fileuploadgo", 0);//进度调整为零
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


}
