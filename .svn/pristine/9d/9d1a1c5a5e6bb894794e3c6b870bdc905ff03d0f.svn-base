package com.starsoft.cms.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("栏目信息")
@Table(name="T_CMS_COLUMNS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Columns extends BaseTreeObject{	
	@InitFieldAnnotation("图片地址")
	private String imgurl;
	@InitFieldAnnotation("是否启用审核")
	private boolean auditState;
	@InitFieldAnnotation("栏目地址")
	private String columnUrl;
	@InitFieldAnnotation("审核角色")
	private String auditRole;
	private List<Article> infoList=new ArrayList<Article>();
	public Columns() {
    	this.id = StringUtil.generatorShort(); 
    }
	@Type(type="yes_no")
	public boolean isAuditState() {
		return auditState;
	}
	public void setAuditState(boolean auditState) {
		this.auditState = auditState;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getColumnUrl() {
		return columnUrl;
	}
	public void setColumnUrl(String columnUrl) {
		this.columnUrl = columnUrl;
	}
	@Transient
	public List<Article> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<Article> infoList) {
		this.infoList = infoList;
	}
	public String getAuditRole() {
		return auditRole;
	}
	public void setAuditRole(String auditRole) {
		this.auditRole = auditRole;
	}
}
