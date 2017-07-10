package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("图片关联")
@Table(name="T_CMS_IMAGEPLAYRELATIONIMAGES")
public class ImagePlayRelationImages extends BaseObject {
	@InitFieldAnnotation("连接文本")
	private String linktext;
	@InitFieldAnnotation("连接地址")
	private String linkurl;
	@InitFieldAnnotation("图片地址")
	private String imageurl;
	@InitFieldAnnotation("播放器标识")
	private String imagePlayId;
	@InitFieldAnnotation("资源标识")
	private String resourceId;
	@InitFieldAnnotation("排序")
	private int sortCode;
	public ImagePlayRelationImages() {
    	this.id = StringUtil.generator(); 
    	this.sortCode=0;
    }
	public String getLinktext() {
		return linktext;
	}
	public void setLinktext(String linktext) {
		this.linktext = linktext;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getImagePlayId() {
		return imagePlayId;
	}
	public void setImagePlayId(String imagePlayId) {
		this.imagePlayId = imagePlayId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public int getSortCode() {
		return sortCode;
	}
	public void setSortCode(int sortCode) {
		this.sortCode = sortCode;
	}
	
}
