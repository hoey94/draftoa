package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("关键字管理")
@Table(name="T_CMS_KEYWORD")
public class KeyWord extends BaseObject {
	@InitFieldAnnotation("连接地址")
	private String url;
	@InitFieldAnnotation("提示信息")
	private String prompt;
	public KeyWord() {
    	this.id = StringUtil.generator();
    }
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
