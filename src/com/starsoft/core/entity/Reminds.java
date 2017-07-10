package com.starsoft.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("我的提醒")
@Table(name="T_CORE_REMINDS")
public class Reminds extends BaseObject {
	@InitFieldAnnotation("接收用户")
	private String receiverId;
	@InitFieldAnnotation("接收用户名字")
	private String receiverName;
	@InitFieldAnnotation(value="提醒内容", maxlength="2048")
	private String description;
	@InitFieldAnnotation(value="课程标识")
	private String courseId;
	@InitFieldAnnotation(value="学校标识")
	private String organId;
	@InitFieldAnnotation(value="提醒时间")
	private Date remindTime;
	public Reminds() {
    	this.id = StringUtil.generator(); 
    	this.setValid(true);//待读
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
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
}
