package com.starsoft.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("提醒")
@Table(name="T_CORE_REMIND")
public class Remind extends BaseObject {
	@InitFieldAnnotation(value="提醒内容", maxlength="2048")
	private String description;
	@InitFieldAnnotation(value="提醒时间")
	private Date remindTime;
	public Remind() {
    	this.id = StringUtil.generator(); 
    }
	@Column(name="tdescription",length=1024)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}
}
