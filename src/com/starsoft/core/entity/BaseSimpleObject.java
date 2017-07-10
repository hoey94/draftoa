package com.starsoft.core.entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;
import com.starsoft.core.util.InitFieldAnnotation;
/***
 * 简单基础类
 * @author Administrator
 */
@MappedSuperclass
public abstract class BaseSimpleObject{
	@InitFieldAnnotation("唯一标识")
	protected String id;
	@InitFieldAnnotation("名称")
	protected String tname;
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
	@Column(name="tname",nullable=false,columnDefinition="VARCHAR(512)")
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id + "]";
	}
}
