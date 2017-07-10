package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

/***
 * 访问量类
 * @author Administrator
 *
 */
@Entity
@InitNameAnnotation("访问历史")
@Table(name="T_CMS_VISTERHISTORY")
public class VisterHistory extends BaseObject {
	@InitFieldAnnotation("请求IP")
	private String ip;
	@InitFieldAnnotation("浏览器类型")
	private String browserType;
	@InitFieldAnnotation("session标识")
	private String sessionId;
	@InitFieldAnnotation("访问路径")
	private String visterpath;
	public VisterHistory() {
    	this.id = StringUtil.generator();
    }
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getVisterpath() {
		return visterpath;
	}
	public void setVisterpath(String visterpath) {
		this.visterpath = visterpath;
	}
	
}
