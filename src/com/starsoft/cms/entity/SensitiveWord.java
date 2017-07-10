package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("敏感字管理")
@Table(name="T_CMS_SENSITIVEWORD")
public class SensitiveWord extends BaseObject {
	public SensitiveWord() {
    	this.id = StringUtil.generator();
    }
}
