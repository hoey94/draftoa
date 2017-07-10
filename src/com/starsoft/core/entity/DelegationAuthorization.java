package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

/***
 * 委托授权
 * @author Administrator
 */
@Entity
@InitNameAnnotation("委托授权")
@Table(name="T_CORE_DELEGATIONAUTHORIZATION")
public class DelegationAuthorization extends BaseObject {
	@InitFieldAnnotation("有效标识")
	private String delegateFrom;
	@InitFieldAnnotation("有效标识")
	private String delegateFromName;
	@InitFieldAnnotation("有效标识")
	private String delegateTo;
	@InitFieldAnnotation("有效标识")
	private String delegateToName;
	public DelegationAuthorization(){
		this.id=StringUtil.generator();
	}
	public String getDelegateFrom() {
		return delegateFrom;
	}
	public void setDelegateFrom(String delegateFrom) {
		this.delegateFrom = delegateFrom;
	}
	public String getDelegateFromName() {
		return delegateFromName;
	}
	public void setDelegateFromName(String delegateFromName) {
		this.delegateFromName = delegateFromName;
	}
	public String getDelegateTo() {
		return delegateTo;
	}
	public void setDelegateTo(String delegateTo) {
		this.delegateTo = delegateTo;
	}
	public String getDelegateToName() {
		return delegateToName;
	}
	public void setDelegateToName(String delegateToName) {
		this.delegateToName = delegateToName;
	}
}
