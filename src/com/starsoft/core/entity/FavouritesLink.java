package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
/***
 * 企业类
 * @author Administrator
 *
 */
@Entity
@InitNameAnnotation("快捷连接收藏")
@Table(name="T_CORE_FAVOURITESlINK")
public class FavouritesLink extends BaseObject {
	@InitFieldAnnotation("连接地址")
	private String link;
	public FavouritesLink() {
    	this.id = StringUtil.generator(); 
    }
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
