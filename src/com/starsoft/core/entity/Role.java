package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("角色")
@Table(name="T_CORE_ROLE")
public class Role extends BaseObject {
	@InitFieldAnnotation("是否允许删除")
	private boolean canDelete;
	public Role() {
    	this.id = StringUtil.generator(); 
	}
	@Type(type="yes_no")
	public boolean isCanDelete() {
		return canDelete;
	}
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
		
}
