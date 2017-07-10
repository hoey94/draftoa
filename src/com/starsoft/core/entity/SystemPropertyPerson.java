package com.starsoft.core.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("系统配置个性化配置")
@Table(name="T_CORE_SYSTEMPROPERTYPERSON")
public class SystemPropertyPerson extends BaseObject {
	@InitFieldAnnotation("系统配置标识")
	private String systemPropertyId;
	@InitFieldAnnotation("配置项描述")
	private String description;
	private List<String> options=new ArrayList<String>();
	public SystemPropertyPerson() {
		this.id = StringUtil.generator();
	}
	@Transient
	public List<String> getOptions() {
		options=StringUtil.toList(tname, ";");
		return options;
	}
	public String getSystemPropertyId() {
		return systemPropertyId;
	}
	public void setSystemPropertyId(String systemPropertyId) {
		this.systemPropertyId = systemPropertyId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
