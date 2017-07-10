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
@InitNameAnnotation("菜单权限")
@Table(name="T_CORE_MENUPRIVILEGE")
public class MenuPrivilege extends BaseObject {
	@InitFieldAnnotation("菜单名称")
	private String menuId;
	@InitFieldAnnotation("角色名称")
	private String roleId;
	public MenuPrivilege() {
    	this.id = StringUtil.generator(); 
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
