package com.starsoft.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("系统日志")
@Table(name="T_CORE_SYSTEMLOG")
public class SystemLog extends BaseObject {
	@InitFieldAnnotation("错误内容")
	private String infoContent;
	@InitFieldAnnotation("请求类")
	private String infoClass;
	@InitFieldAnnotation("异常类名")
	private String exceptionClass;
	@InitFieldAnnotation("请求路径")
	private String qpath;
	@InitFieldAnnotation("请求IP")
	private String ip;
	@InitFieldAnnotation("浏览器类型")
	private String browserType;
	public SystemLog() {
    	this.id = StringUtil.generator(); 
    }
	@Column(length=1024)
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public String getInfoClass() {
		return infoClass;
	}
	public void setInfoClass(String infoClass) {
		this.infoClass = infoClass;
	}
	public String getQpath() {
		return qpath;
	}
	public void setQpath(String qpath) {
		this.qpath = qpath;
	}
	@Column(length=32)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column(length=200)
	public String getExceptionClass() {
		return exceptionClass;
	}
	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
}
