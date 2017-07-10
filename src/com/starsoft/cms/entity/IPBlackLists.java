package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("IP黑名单")
@Table(name="T_CMS_REFUND")
public class IPBlackLists extends BaseObject {
	private String ip;
	private String gateway;
	public IPBlackLists(){
		this.id=StringUtil.generator();
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	
}
