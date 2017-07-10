package com.starsoft.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("使用样例")
@Table(name="T_CORE_EXAMPLE")
public class Example extends BaseObject {
	@InitFieldAnnotation("信息内容")
	private String content;
	public Example() {
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
