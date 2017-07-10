package com.starsoft.cms.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("文章信息")
@Table(name="T_CMS_ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article  extends BaseObject{	
	@InitFieldAnnotation("审核人")
	private String auditer;
	@InitFieldAnnotation("审核状态")
	private boolean auditState;
	@InitFieldAnnotation("审核时间")
	private Date auditTime;
	@InitFieldAnnotation("发布时间")
	private Date publishTime;
	@InitFieldAnnotation("点击数")
	private Integer hits;
	@InitFieldAnnotation("静态地址")
	private String staticUrl;
	@InitFieldAnnotation("栏目id")
	private String columnId;
	@InitFieldAnnotation("文章标题图")
	private String imgUrl;
	@InitFieldAnnotation("摘要")
	private String summary;	
	@InitFieldAnnotation("信息来源")
	private String inforsource;
	@InitFieldAnnotation("信息排序")
	private int sortCode;
		
	public Article() {
    	this.id = StringUtil.generatorShort();
    	this.sortCode=0;
    	this.publishTime=new Date();
    }
	public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
	public String getStaticUrl() {
		return staticUrl;
	}
	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}
	public String getAuditer() {
		return auditer;
	}
	public void setAuditer(String auditer) {
		this.auditer = auditer;
	}
	@Type(type="yes_no")
	public boolean isAuditState() {
		return auditState;
	}
	public void setAuditState(boolean auditState) {
		this.auditState = auditState;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getInforsource() {
		return inforsource;
	}
	public void setInforsource(String inforsource) {
		this.inforsource = inforsource;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public int getSortCode() {
		return sortCode;
	}
	public void setSortCode(int sortCode) {
		this.sortCode = sortCode;
	}
	public String getImgUrl() {
		return imgUrl;
	}
}
