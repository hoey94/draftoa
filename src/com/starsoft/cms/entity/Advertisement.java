package com.starsoft.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("广告管理")
@Table(name="T_CMS_ADVERTISEMENT")
public class Advertisement extends BaseObject {
	/***
	 * 广告类型 A:单图浮动广告 B:左侧浮动广告 C：右侧浮动广告 D 垂直左边广告 E:垂直右边广告 F:顶部浮动广告 G:底部浮动广告 
	 */
	@InitFieldAnnotation("广告类型")
	private String advertType;
	/**
	 * 第一个张图片
	 */
	@InitFieldAnnotation("广告图片")
	private String imgUrl;
	/***
	 * 第一章图片链接地址
	 */
	@InitFieldAnnotation("链接地址")
	private String imgLink;
	/***
	 * 图片的宽度 单位 px
	 */
	@InitFieldAnnotation("图片宽度")
	private Integer imgWidth;
	/***
	 * 图片的高度 单位 px
	 */
	@InitFieldAnnotation("图片高度")
	private Integer imgHeight;
	/***
	 * 靠近顶部的距离 单位 px
	 */
	@InitFieldAnnotation("顶部距离")
	private Integer topSpace;
	/**
	 * 靠近右边距离
	 */
	@InitFieldAnnotation("左边距离")
	private Integer leftSpace;
	/***
	 * 摘要
	 */
	@InitFieldAnnotation("摘要")
	private String  sommaire;
	

	public Advertisement() {
    	this.id = StringUtil.generator();
    }
	public String getAdvertType() {
		return advertType;
	}
	public void setAdvertType(String advertType) {
		this.advertType = advertType;
	}
	public Integer getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(Integer imgWidth) {
		this.imgWidth = imgWidth;
	}
	public Integer getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(Integer imgHeight) {
		this.imgHeight = imgHeight;
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
	@Column(name="ttopSpace")
	public Integer getTopSpace() {
		return topSpace;
	}
	public void setTopSpace(Integer topSpace) {
		this.topSpace = topSpace;
	}
	public Integer getLeftSpace() {
		return leftSpace;
	}
	public void setLeftSpace(Integer leftSpace) {
		this.leftSpace = leftSpace;
	}
	

}
