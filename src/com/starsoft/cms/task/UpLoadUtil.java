package com.starsoft.cms.task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.multipart.MultipartFile;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.vo.FileUpload;
import com.starsoft.oa.entity.Motion;

public class UpLoadUtil {
	public static String myUpLoad(FileUpload entity) throws Exception,
			IOException, FileNotFoundException {
		String upLoadPath =createFolder();
		MultipartFile file = entity.getFile();
		String filename = file.getOriginalFilename();
		Long size=file.getSize();
		String fileid=new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date()).substring(0, 14);
		String currentFileType = filename.substring(
				filename.lastIndexOf(".") + 1).toLowerCase();
		if (typeMap().indexOf(currentFileType) != -1&&(size<10*1024*1024)) {
			BufferedInputStream bis = new BufferedInputStream(
					file.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(upLoadPath +fileid+ filename));
			Streams.copy(bis, bos, true);
			return upLoadPath +fileid+ filename;
		}else{
			return "";
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @return
	 */
	public static String createFolder() {
		String uploadPath = "E:\\systemInfo\\";// 要发布的系统
		if (!(new File(uploadPath).isDirectory())) {
			new File(uploadPath).mkdir();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		uploadPath = uploadPath + "upload\\"; // 选定上传的目录此处为当前目录

		if (!(new File(uploadPath).isDirectory())) {
			new File(uploadPath).mkdir();
			uploadPath = uploadPath + sdf.format(new Date()).substring(0, 6)
					+ "\\"; // 选定上传的目录此处为当前目录
			new File(uploadPath).mkdir();
		} else {
			uploadPath = uploadPath + sdf.format(new Date()).substring(0, 6)
					+ "\\"; // 选定上传的目录此处为当前目录
			if (!(new File(uploadPath).isDirectory())) {
				new File(uploadPath).mkdir();
			}
		}
		return uploadPath;
	}

	// 可上传文件类型
	public static List<String> typeMap() {
		List<String> list = new ArrayList<String>();
		list.add("txt");
		list.add("doc");
		list.add("docx");
		return list;
	}
}
