package com.starsoft.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("登录日志记录")
@Table(name="T_CORE_LOGINRECORD")
public class LogInRecord extends BaseObject{
	@InitFieldAnnotation("请求IP")
	private String ip;
	@InitFieldAnnotation("浏览器类型")
	private String browserType;
	public LogInRecord() {
    	this.id = StringUtil.generator(); 
    }
	@Column(length=32)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id + "]";
	}
	@Column(length=100)
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
}
