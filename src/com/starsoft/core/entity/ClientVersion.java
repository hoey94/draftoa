package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("客户端管理")
@Table(name="T_CORE_CLIENTVERSION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClientVersion extends BaseObject{
	@InitFieldAnnotation("下载路径")
	private String downPath;
	@InitFieldAnnotation("客户端代号")
	private String clientCode;
	@InitFieldAnnotation("当前版本号")
	private String currVersion;
	@InitFieldAnnotation("客户端类型")
	private String clientType;
	public ClientVersion(){
		this.id=StringUtil.generatorShort();
	}
	public String getDownPath() {
		return downPath;
	}
	public void setDownPath(String downPath) {
		this.downPath = downPath;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getCurrVersion() {
		return currVersion;
	}
	public void setCurrVersion(String currVersion) {
		this.currVersion = currVersion;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

}
