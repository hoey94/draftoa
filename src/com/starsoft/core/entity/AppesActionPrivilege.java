package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
/***
 * 权限管理
 * @author Administrator
 *
 */
@Entity
@InitNameAnnotation("操作权限")
@Table(name="T_CORE_APPESACTIONPRIVILEGE")
public class AppesActionPrivilege extends BaseObject {
	@InitFieldAnnotation("应用程序")
	private String appes;
	@InitFieldAnnotation("角色名称")
	private String roleId;
	public AppesActionPrivilege() {
    	this.id = StringUtil.generator(); 
	}
	public String getAppes() {
		return appes;
	}
	public void setAppes(String appes) {
		this.appes = appes;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	
}
