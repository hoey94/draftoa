package com.starsoft.cms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("采集规则")
@Table(name="T_CMS_ACQUISITIONRULE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AcquisitionRule extends BaseObject {
	@InitFieldAnnotation("栏目")
	private Columns columns;
	@InitFieldAnnotation("网页编码")
	private String pageCharset;
	@InitFieldAnnotation("采集内容数")
	private String contentLength;
	@InitFieldAnnotation("是否把首幅图片设置为标题图片")
	private boolean titleImage; 
	@InitFieldAnnotation("网页地址")
	private String pageUrl;
	@InitFieldAnnotation("列表地址")
	private String listUrl;
	@InitFieldAnnotation("地址开始")
	private String pageHrefStart;
	@InitFieldAnnotation("标题开始")
	private String titleStart;
	@InitFieldAnnotation("内容开始")
	private String contentStart;
	@InitFieldAnnotation("内容HTML清除")
	private boolean clearHtml;
	@InitFieldAnnotation("开始时间")
	private Date startTime;
	@InitFieldAnnotation("结束时间")
	private Date endTime;
	
	public AcquisitionRule() {
    	this.id = StringUtil.generator(); 
    }

	public String getPageCharset() {
		return pageCharset;
	}

	public void setPageCharset(String pageCharset) {
		this.pageCharset = pageCharset;
	}

	public String getContentLength() {
		return contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}
	@Type(type="yes_no")
	public boolean isTitleImage() {
		return titleImage;
	}

	public void setTitleImage(boolean titleImage) {
		this.titleImage = titleImage;
	}
	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getTitleStart() {
		return titleStart;
	}

	public void setTitleStart(String titleStart) {
		this.titleStart = titleStart;
	}
	@Type(type="yes_no")
	public boolean isClearHtml() {
		return clearHtml;
	}

	public void setClearHtml(boolean clearHtml) {
		this.clearHtml = clearHtml;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column(name="tstarttime")
	public Date getStartTime() {
		return startTime;
	}
	@Column(name="tendtime")
	public Date getEndTime() {
		return endTime;
	}

	@ManyToOne
	@JoinColumn(name = "columnsId",referencedColumnName="id")
	public Columns getColumns() {
		return columns;
	}

	public void setColumns(Columns columns) {
		this.columns = columns;
	}

	public String getContentStart() {
		return contentStart;
	}

	public void setContentStart(String contentStart) {
		this.contentStart = contentStart;
	}

	public void setPageHrefStart(String pageHrefStart) {
		this.pageHrefStart = pageHrefStart;
	}
	@Column(length=512)
	public String getPageHrefStart() {
		return pageHrefStart;
	}

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}
	

}
