package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("注册用户人数")
@Table(name="T_EDU_USERNUMBER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserNumber extends BaseObject{
	@InitFieldAnnotation("注册人数")
	private int userNum;
	
	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public UserNumber(){
		this.id=StringUtil.generator();
	}

}
