package com.starsoft.core.entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.starsoft.core.util.InitFieldAnnotation;
/***
 * 基础类
 * @author Administrator
 */
@MappedSuperclass
public abstract class BaseObject implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2408296184848472202L;
	/**
	 * 
	 */
	@InitFieldAnnotation("唯一标识")
	protected String id;
	@InitFieldAnnotation("名称")
	protected String tname;
	@InitFieldAnnotation("有效标识")
	private boolean valid;
	@InitFieldAnnotation("创建者标识")
	private String createId;
	@Id
	@GenericGenerator(name="generator",strategy="assigned")
	@GeneratedValue(generator="generator")
	@Column(length=32,unique=true,nullable=false,updatable=false,insertable=true)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Type(type="yes_no")
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	@Column(name="tname",nullable=false,columnDefinition="VARCHAR(512)")
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	@Column(length=32,updatable = false)
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id + "]";
	}
}
