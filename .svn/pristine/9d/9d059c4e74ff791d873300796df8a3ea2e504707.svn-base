package com.starsoft.cms.entity;

import java.sql.Clob;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("模版管理")
@Table(name="T_CMS_ARTICLETEMPLATE")
public class ArticleTemplate extends BaseObject {
	@InitFieldAnnotation("模版摘要")
	private String summary;
	@InitFieldAnnotation("信息内容")
	private Clob templateContent;
	public ArticleTemplate() {
    	this.id = StringUtil.generatorShort();
    }
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Clob getTemplateContent() {
		return templateContent;
	}
	public void setTemplateContent(Clob templateContent) {
		this.templateContent = templateContent;
	}
}
