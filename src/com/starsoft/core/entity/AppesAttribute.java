package com.starsoft.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@Table(name="T_CORE_APPESATTRIBUTE")
@InitNameAnnotation("应用属性")
public class AppesAttribute extends BaseObject {
	@InitFieldAnnotation("应用Id")
	private String appes;
	@InitFieldAnnotation("类型")
	private String attributeType;
	@InitFieldAnnotation("属性说明")
	private String description;
	@InitFieldAnnotation("显示类型")
	private String displayType;
	@InitFieldAnnotation("增加页面显示")
	private boolean addDisplay;
	@InitFieldAnnotation("编辑页面显示")
	private boolean editDisplay;
	@InitFieldAnnotation("是否列表显示")
	private boolean listDisplay;
	@InitFieldAnnotation("允许空值")
	private boolean nullValue;
	@InitFieldAnnotation("最大长度")
	private Integer maxLength;
	@InitFieldAnnotation("where条件")
	private String whereParameter;
	@InitFieldAnnotation("排序号")
	private int sortCode;
	public AppesAttribute() {
	    	this.id = StringUtil.generator(); 
	    	this.sortCode=0;
	}
	public String getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	@Type(type="yes_no")
	public boolean isAddDisplay() {
		return addDisplay;
	}
	public void setAddDisplay(boolean addDisplay) {
		this.addDisplay = addDisplay;
	}
	@Type(type="yes_no")
	public boolean isEditDisplay() {
		return editDisplay;
	}
	public void setEditDisplay(boolean editDisplay) {
		this.editDisplay = editDisplay;
	}
	@Type(type="yes_no")
	public boolean isListDisplay() {
		return listDisplay;
	}
	public void setListDisplay(boolean listDisplay) {
		this.listDisplay = listDisplay;
	}
	@Type(type="yes_no")
	public boolean isNullValue() {
		return nullValue;
	}
	public void setNullValue(boolean nullValue) {
		this.nullValue = nullValue;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public String getWhereParameter() {
		return whereParameter;
	}
	public void setWhereParameter(String whereParameter) {
		this.whereParameter = whereParameter;
	}
	@Column(length=32)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAppes() {
		return appes;
	}
	public void setAppes(String appes) {
		this.appes = appes;
	}
	@Column(columnDefinition="int default 0")
	public int getSortCode() {
		return sortCode;
	}
	public void setSortCode(int sortCode) {
		this.sortCode = sortCode;
	}
}
