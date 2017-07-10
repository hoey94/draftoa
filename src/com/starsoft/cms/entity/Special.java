package com.starsoft.cms.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("专题报道")
@Table(name="T_CMS_SPECIAL")
public class Special extends BaseObject {
	@InitFieldAnnotation("专题摘要")
	private String summary;	
	@InitFieldAnnotation("专题模版")
	private String template;
	@InitFieldAnnotation("开始时间")
	private Date startDate;
	@InitFieldAnnotation("结束时间")
	private Date endDate;
	public Special() {
    	this.id = StringUtil.generator(); 
    }
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
