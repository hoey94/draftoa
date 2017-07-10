package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("群组人员")
@Table(name="T_CORE_GROUPUSERS")
public class GroupUsers extends BaseObject{
	@InitFieldAnnotation("群组标识")
	private String groupId;
	@InitFieldAnnotation("用户")
	private String usersId;
	public GroupUsers() {
	    this.id = StringUtil.generator(); 
	}
	public String getUsersId() {
		return usersId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public void setUsersId(String usersId) {
		this.usersId = usersId;
	} 
}
