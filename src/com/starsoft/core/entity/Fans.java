package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("粉丝")
@Table(name="T_CORE_FANS")
public class Fans extends BaseObject {
	@InitFieldAnnotation("粉丝标识")
	private String fansId;
	public Fans(){
		this.id=StringUtil.generator();
	}
	public String getFansId() {
		return fansId;
	}
	public void setFansId(String fansId) {
		this.fansId = fansId;
	}
}
