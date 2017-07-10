package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("我的通讯录")
@Table(name="T_CORE_ADDRESSBOOK")
public class AddressBook extends BaseObject {
	@InitFieldAnnotation("手机号码")
	private String mobilNumber;
	@InitFieldAnnotation("电子邮箱")
	private String email;
	@InitFieldAnnotation("QQ号码")
	private String qqNumber;
	@InitFieldAnnotation("家庭地址")
	private String homeAddress;
	@InitFieldAnnotation("工作地址")
	private String workAddress;
	@InitFieldAnnotation("通讯录组")
	private String addressBookTypeId;
	public AddressBook() {
    	this.id = StringUtil.generator(); 
	}
	public String getMobilNumber() {
		return mobilNumber;
	}
	public void setMobilNumber(String mobilNumber) {
		this.mobilNumber = mobilNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQqNumber() {
		return qqNumber;
	}
	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	public String getAddressBookTypeId() {
		return addressBookTypeId;
	}
	public void setAddressBookTypeId(String addressBookTypeId) {
		this.addressBookTypeId = addressBookTypeId;
	}

}
