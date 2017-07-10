package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("通讯录组")
@Table(name="T_CORE_ADDRESSBOOKTYPE")
public class AddressBookType extends BaseTreeObject {
	public AddressBookType() {
    	this.id = "TXL"+StringUtil.generatorShort();
	}
}
