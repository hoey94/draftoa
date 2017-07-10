package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

/***
 * 工作岗位
 * @author Administrator
 *
 */
@Entity
@InitNameAnnotation("工作岗位管理")
@Table(name="T_CORE_WORKPOST")
public class JobPost extends BaseObject {
	public JobPost() {
    	this.id = StringUtil.generator(); 
	} 

}
