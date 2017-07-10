package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("广告管理")
@Table(name="T_CMS_ADVERTISEMENTBOTTOM")
public class AdvertisementBottom extends BaseObject {
	@InitFieldAnnotation("图片地址")
	private String imgUrl;
	@InitFieldAnnotation("链接地址")
	private String imgLink;
	@InitFieldAnnotation("摘要")
	private String  sommaire;
	
	public AdvertisementBottom(){
		this.id=StringUtil.generator();
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgLink() {
		return imgLink;
	}
	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}
	public String getSommaire() {
		return sommaire;
	}
	public void setSommaire(String sommaire) {
		this.sommaire = sommaire;
	}

}
