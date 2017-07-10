package com.starsoft.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("待阅事项")
@Table(name="T_CORE_WAITTOREAD")
public class WaitToRead extends BaseObject {
	@InitFieldAnnotation("待阅读人")
	private String transactId;
	@InitFieldAnnotation("待读类型")
	private String toReadType;
	@InitFieldAnnotation("阅读时间")
	private Date transactTime;
	@InitFieldAnnotation("待阅读内容")
	private String content;
	public WaitToRead() {
    	this.id = StringUtil.generator(); 
    }
	@Column(length=1024)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTransactId() {
		return transactId;
	}
	public void setTransactId(String transactId) {
		this.transactId = transactId;
	}
	public String getToReadType() {
		return toReadType;
	}
	public void setToReadType(String toReadType) {
		this.toReadType = toReadType;
	}
	public Date getTransactTime() {
		return transactTime;
	}
	public void setTransactTime(Date transactTime) {
		this.transactTime = transactTime;
	}
	
	
}
