package com.starsoft.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("系统配置")
@Table(name="T_CORE_SYSTEMPROPERTY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemProperty extends BaseObject {
	@InitFieldAnnotation("配置项描述")
	private String description;
	private List<String> options=new ArrayList<String>();
	public SystemProperty() {
		this.id = StringUtil.generator();
	}
	@Column(length=512)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Transient
	public List<String> getOptions() {
		options=StringUtil.toList(tname, ";");
		return options;
	}
}
