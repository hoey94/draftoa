package com.starsoft.cms.task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author 赵一好
 * springmvc下载功能
 */
public class DownLoadUtil {

	/**
	 * 
	 * @param request
	 * @param response
	 * @param path  存在数据的全路径 eg E:\\systemInfo\\upload\201705\\201705191125101.docx
	 * @param fileName  文件名 eg 201705191125101.docx
	 * @throws Exception
	 */
	public static void download(HttpServletRequest request,
			HttpServletResponse response, String path, String fileName)
			throws Exception {

		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		long fileLength = new File(path).length();

		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(path));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

}
