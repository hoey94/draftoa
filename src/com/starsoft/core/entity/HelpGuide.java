package com.starsoft.core.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("帮助向导")
@Table(name="T_CORE_HELPGRIDE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HelpGuide extends BaseObject {
	@InitFieldAnnotation("帮助内容")
	private String content;	
    public HelpGuide() {
    	this.id = StringUtil.generator();
    } 
	@Column(name="tcontent",length=2048)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
