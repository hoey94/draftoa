package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@Table(name="T_CORE_APPESACTION")
@InitNameAnnotation("模块操作")
public class AppesAction extends BaseObject {
	@InitFieldAnnotation("应用名称")
	private String appes;
	private String actionType;
	public AppesAction(){
    	this.id = StringUtil.generator(); 
	}
	/***
	 * 类别有 list form select
	 * @return
	 */
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getAppes() {
		return appes;
	}
	public void setAppes(String appes) {
		this.appes = appes;
	}
	
}
