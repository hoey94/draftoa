package com.starsoft.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("待办事项")
@Table(name="T_CORE_WAITTODO")
public class WaitToDo extends BaseObject {
	@InitFieldAnnotation("办理人标识")
	private String transactId;
	@InitFieldAnnotation("受理时间")
	private Date transactTime;
	@InitFieldAnnotation("截至时间")
	private Date endTime;
	@InitFieldAnnotation("待办类型")
	private String toDoType;
	@InitFieldAnnotation("待办地址")
	private String url;
	public WaitToDo() {
    	this.id = StringUtil.generator(); 
    } 
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getToDoType() {
		return toDoType;
	}
	public void setToDoType(String toDoType) {
		this.toDoType = toDoType;
	}
	public String getTransactId() {
		return transactId;
	}
	public void setTransactId(String transactId) {
		this.transactId = transactId;
	}
	public Date getTransactTime() {
		return transactTime;
	}
	public void setTransactTime(Date transactTime) {
		this.transactTime = transactTime;
	}


}
