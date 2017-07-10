package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("采集历史")
@Table(name="T_CMS_ACQUISITIONHISTORY")
public class AcquisitionHistory extends BaseObject {
	@InitFieldAnnotation("采集地址")
	private String url;
	public AcquisitionHistory() {
    	this.id = StringUtil.generator(); 
    }

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
