package com.starsoft.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("全文检索")
@Table(name="T_CMS_INDEXSEARCH")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IndexSearch implements Serializable {
	private static final long serialVersionUID = 1L;
	@InitFieldAnnotation("唯一标识")
	private String id;
	@InitFieldAnnotation("标题")
	private String tname;
	@InitFieldAnnotation("查询关键字")
	private String searchKey;
	@InitFieldAnnotation("查询路径")
	private String querypath;
	@InitFieldAnnotation("关联标识")
	private String baseObjectId;
	@InitFieldAnnotation("创建时间")
	private Date createTime;
	public IndexSearch(){
		this.id=StringUtil.generator();
		this.createTime=new Date();
	}
	public IndexSearch(String tname,String searchKey,String querypath,String baseObjectId){
		this.id=StringUtil.generator();
		this.createTime=new Date();
		this.tname=tname;
		this.searchKey=searchKey;
		this.querypath=querypath;
		this.baseObjectId=baseObjectId;
	}
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
	@Column(length=200,name="tname",nullable=false,unique=false)
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	@Column(length=100,name="querypath",nullable=false)
	public String getQuerypath() {
		return querypath;
	}
	public void setQuerypath(String querypath) {
		this.querypath = querypath;
	}
	public String getBaseObjectId() {
		return baseObjectId;
	}
	public void setBaseObjectId(String baseObjectId) {
		this.baseObjectId = baseObjectId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(length=400,name="searchKey",nullable=false)
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	
	
}
