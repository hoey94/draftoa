package com.starsoft.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.starsoft.core.util.InitFieldAnnotation;

/***
 * 基础类树型结构
 * @author Administrator
 *
 */
@MappedSuperclass
public abstract class BaseTreeObject extends BaseObject {
	@InitFieldAnnotation("排序号")
	private int sortCode;
	/****
	 * 上级标识，顶级组织为32个1
	 */
	@InitFieldAnnotation("上级标识")
	protected String parentId;
	@InitFieldAnnotation("下级子集")
	private List<BaseTreeObject> subset=new ArrayList<BaseTreeObject>();
	@Column(length=32)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Transient
	public List<BaseTreeObject> getSubset() {
		return subset;
	}

	public void setSubset(List<BaseTreeObject> subset) {
		this.subset = subset;
	}

	@Column(columnDefinition="int default 0")
	public int getSortCode() {
		return sortCode;
	}
	public void setSortCode(int sortCode) {
		this.sortCode = sortCode;
	}
}
