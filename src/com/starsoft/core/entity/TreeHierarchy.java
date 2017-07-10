package com.starsoft.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("树形结构层级")
@Table(name="T_CORE_TREEHIERARCHY")
public class TreeHierarchy extends BaseObject {
	/****
	 * 上级标识，顶级组织为32个1
	 */
	@InitFieldAnnotation("上级标识")
	protected String parentId;
	@InitFieldAnnotation("下级标识")
	private String childId;
	@InitFieldAnnotation("应用标识")
	private String appes;
	public TreeHierarchy() {
		this.id = StringUtil.generator();
	}
	@Column(length=32)
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	@Column(length=32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Column(length=32)
	public String getAppes() {
		return appes;
	}
	public void setAppes(String appes) {
		this.appes = appes;
	}
	
}
