package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@Table(name="T_CORE_SSO")
@InitNameAnnotation("单点登录信息记录")
public class SSO extends BaseObject {
	private String sessionId;
	public SSO(){
    	this.id = StringUtil.generator(); 
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
