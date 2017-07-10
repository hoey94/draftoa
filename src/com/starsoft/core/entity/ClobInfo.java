package com.starsoft.core.entity;

import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
@Entity
@InitNameAnnotation("详细信息")
@Table(name="T_CORE_CLOBINFO")
public class ClobInfo {
	@InitFieldAnnotation("唯一标识")
	protected String id;
	@InitFieldAnnotation("信息内容")
	private Clob content;
	public ClobInfo(){
	}
	public ClobInfo(String id){
    	this.id = id; 
	}
	@Id
	@GenericGenerator(name="generator",strategy="assigned")
	@GeneratedValue(generator="generator")
	@Column(length=32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Clob getContent() {
		return content;
	}
	public void setContent(Clob content) {
		this.content = content;
	}
}
