package com.starsoft.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("连接管理")
@Table(name="T_CMS_LINKS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Links extends BaseObject {
	@InitFieldAnnotation("连接类型")
	private String linkType;
	@InitFieldAnnotation("连接地址")
	private String url;
	@InitFieldAnnotation("图片地址")
	private String imageurl;
	public Links() {
    	this.id = StringUtil.generator(); 
    }
	@Column(length=32)
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	@Column(length=255)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(length=128)
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
}
