package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("部门管理员")
@Table(name="T_CORE_ORGANADMINISTRATOR")
public class OrganAdministrator extends BaseObject{
	@InitFieldAnnotation("部门标识")
	private String organId;
	@InitFieldAnnotation("部门管理员")
	private String usersId;
	public OrganAdministrator() {
    	this.id = StringUtil.generator(); 
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getUsersId() {
		return usersId;
	}
	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}
	

}
