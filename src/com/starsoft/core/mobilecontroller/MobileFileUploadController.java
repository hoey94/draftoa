package com.starsoft.core.mobilecontroller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.util.GsonUtil;
import com.starsoft.core.util.HttpResponseResult;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.FileUploadImg;


public class MobileFileUploadController extends BaseAjaxController {
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpResponseResult responseBean = new HttpResponseResult();
		try {
			String uploadPath = createFolder();    //D:\\systemInfo\\upload
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			String fileNameResult="/systemInfo/upload/"+sdf.format(new Date()).substring(0,6)+"/";
			FileUploadImg entity=new FileUploadImg();
			this.bind(request, entity);
			CommonsMultipartFile file = entity.getImg();
			if (file != null && file.getSize() > 0) {
				String filename = file.getOriginalFilename();
				String filelinkname = sdf.format(new Date())
						+ filename.substring(filename.lastIndexOf("."));
				BufferedInputStream in = new BufferedInputStream(
						file.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File(uploadPath + "\\"
								+ filename)));
				Streams.copy(in, out, true); // 开始把文件写到你指定的上传文件夹
				fileNameResult = fileNameResult + filelinkname;
				responseBean.setResultCode(HttpResponseResult.SUCCESS);
				responseBean.setData(WEBCONSTANTS.getSystemProperty("systempath")+fileNameResult);
				responseBean.setResultDesc("上传成功！");
			}
		} catch (Exception e) {
			responseBean.setResultCode(HttpResponseResult.FAIL);
			responseBean.setResultDesc("上传失败！");
			logger.error("HttpDealError", e);
		} finally {
			// 输出数据
			try {
				if (responseBean == null) {
					responseBean=new HttpResponseResult();
					responseBean.setResultCode(HttpResponseResult.FAIL);
					responseBean.setResultDesc("上传失败！");
				}
				if (responseBean.getData() == null) {
					responseBean.setData("");
				}
				String output = GsonUtil.getJson(responseBean);
				logger.info("OUTPUT:" + output);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/xml");
				PrintWriter outWriter = response.getWriter();
				outWriter.write(output);
				outWriter.flush();
				outWriter.close();
			} catch (Exception e2) {
				logger.error("HttpOutputError", e2);
			}
		}
		return null;
	}
	public String createFolder(){
		String uploadPath = "D:\\systemInfo\\upload\\";//要发布的系统 
		if(!(new File(uploadPath).isDirectory())){
			new File(uploadPath).mkdir();
		}
		return uploadPath;
	}
	/***
	 * 流信息写入到文件
	 * @param ins
	 * @param file
	 */
	public void inputstreamtofile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
