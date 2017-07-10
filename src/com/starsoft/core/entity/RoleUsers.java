package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("角色人员")
@Table(name="T_CORE_ROLEUSERS")
public class RoleUsers extends BaseObject{
	@InitFieldAnnotation("角色")
	private String roleId;
	@InitFieldAnnotation("用户")
	private String usersId;
	public RoleUsers() {
	    this.id = StringUtil.generator(); 
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUsersId() {
		return usersId;
	}
	public void setUsersId(String usersId) {
		this.usersId = usersId;
	} 
}
