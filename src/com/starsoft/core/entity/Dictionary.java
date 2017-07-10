package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("字典值")
@Table(name="T_CORE_DICTIONARY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dictionary extends BaseObject {
	@InitFieldAnnotation("属性说明")
	private String description;
	@InitFieldAnnotation("默认值")
	private String defaultValue;
	public Dictionary() {
    	this.id = StringUtil.generator(); 
    }
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
