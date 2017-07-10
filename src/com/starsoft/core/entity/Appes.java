package com.starsoft.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;

@Entity
@Table(name="T_CORE_APPES")
@InitNameAnnotation("应用")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Appes extends BaseObject {
	@InitFieldAnnotation("物理路径")
	private String classPath;
	@InitFieldAnnotation("类名")
	private String className;
	@InitFieldAnnotation("控制器类名")
	private String controllerClassName;
	@InitFieldAnnotation("子系统名称")
	private String subSystemName;
	@InitFieldAnnotation("数据表名")
	private String tableName;
	@InitFieldAnnotation("工作流标识")
	private String workFlowId;
	@InitFieldAnnotation("App对应的操作")
	private List<AppesAction> actionList=new ArrayList<AppesAction>();
	@InitFieldAnnotation("App对应的数据库字段")
	private List<AppesAttribute> attributeList=new ArrayList<AppesAttribute>();
	@InitFieldAnnotation("App对应的数据库字段")
	private Map<String,AppesAttribute> attributeMap=new HashMap<String,AppesAttribute>();
	/***
	 * 创建时间
	 */
	private Date createTime=new Date();
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Column(length=32)
	public String getSubSystemName() {
		return subSystemName;
	}
	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}
	@Column(length=64)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Transient
	public List<AppesAction> getActionList() {
		return actionList;
	}
	public void setActionList(List<AppesAction> actionList) {
		this.actionList = actionList;
	}
	@Transient
	public List<AppesAttribute> getAttributeList() {
		return attributeList;
	}
	public void setAttributeList(List<AppesAttribute> attributeList) {
		this.attributeList = attributeList;
	}
	public String getControllerClassName() {
		return controllerClassName;
	}
	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
	}
	@Transient
	public Map<String, AppesAttribute> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(Map<String, AppesAttribute> attributeMap) {
		this.attributeMap = attributeMap;
	}
	/****
	 * 判断是否包含属性
	 * @param appesAttribute
	 * @return
	 */
	@Transient
	public boolean containsAttribute(String appesAttribute){
		return attributeMap.containsKey(appesAttribute);
	}
	@Column(length=32)
	public String getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(String workFlowId) {
		this.workFlowId = workFlowId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
