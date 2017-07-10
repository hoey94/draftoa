package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("群组管理")
@Table(name="T_CORE_GROUP")
public class Group extends BaseObject {
	public Group() {
    	this.id = StringUtil.generator(); 
	}
}
