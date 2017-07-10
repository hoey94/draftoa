package com.starsoft.core.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUploadImg {
	/***
	 * 图片上传辅助用
	 */
	private CommonsMultipartFile img;
	
	private String fileTypes;
	
	private String fileType;

	public String getFileTypes() {
		return fileTypes.toLowerCase();
	}

	public void setFileTypes(String fileTypes) {
		this.fileTypes = fileTypes;
	}

	public CommonsMultipartFile getImg() {
		return img;
	}

	public void setImg(CommonsMultipartFile img) {
		this.img = img;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
