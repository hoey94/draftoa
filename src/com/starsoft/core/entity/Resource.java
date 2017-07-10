package com.starsoft.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@Table(name="T_CORE_RESOURCE")
@InitNameAnnotation("资源管理")
public class Resource extends BaseObject {
	@InitFieldAnnotation("文件类型")
	private String fileType;
	@InitFieldAnnotation("文件路径")
	private String url;
	@InitFieldAnnotation("关联标识")
	private String baseObjectId;
	public Resource(){
    	this.id = StringUtil.generator(); 
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(length=32,updatable = false)
	public String getBaseObjectId() {
		return baseObjectId;
	}
	public void setBaseObjectId(String baseObjectId) {
		this.baseObjectId = baseObjectId;
	}
	
	
}
