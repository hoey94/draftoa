package com.starsoft.core.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import com.starsoft.core.task.FileUploadListener;


/**
 * Servlet implementation class FileUpload
 */
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadPath =this.createFolder();
		request.setAttribute("doAction", "eturnmsg();");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS"); 
		String fileNameResult="/systemInfo/upload/"+sdf.format(new Date()).substring(0,6)+"/";
		HttpSession session = request.getSession(true);
		request.setAttribute("msg", fileNameResult);
		try{
				FileUploadListener uploadProgressListener = new FileUploadListener();
				uploadProgressListener.setSession(session);
				
			  	DiskFileItemFactory factory = new DiskFileItemFactory();
		        ServletFileUpload fileUpload = new ServletFileUpload(factory);
		        fileUpload.setProgressListener(uploadProgressListener);
		        long size = 419430400;//上传文件大小控制
		        fileUpload.setSizeMax(size);   
		        List items = fileUpload.parseRequest(request);
		        if(items.size()!=0)
		        {
		        	Iterator iter= items.iterator();
		        	while(iter.hasNext()) {
				           FileItem file = (FileItem) iter.next();
				           if(file!=null&&file.getSize()>0){
								String filename = file.getName();
								String currentFileType = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
								if(!"mp4".equals(currentFileType)){//判断类型
									request.setAttribute("err", "上传失败！文件类型不允许！仅能上传类型："+"mp4");
									request.setAttribute("msg", fileNameResult);
								}else {
									String filelinkname = sdf.format(new Date())+filename.substring(filename.lastIndexOf("."));   
									BufferedInputStream in = new BufferedInputStream(file.getInputStream());  
									BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(uploadPath + "\\" + filelinkname)));   
					            	Streams.copy(in, out, true);
					            	fileNameResult = fileNameResult + filelinkname;
					            	request.setAttribute("msg", fileNameResult);
								}
				           	} 	
		        		}
		        }
		       
		}catch(Exception e){
			e.getMessage();
			request.setAttribute("err", "上传失败！");
			request.setAttribute("msg", fileNameResult);
		}

		RequestDispatcher rd = request.getRequestDispatcher("page/common/javascriptNewpageMP4.jsp");
		rd.forward(request, response);
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
