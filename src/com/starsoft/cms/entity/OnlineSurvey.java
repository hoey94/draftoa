package com.starsoft.cms.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("在线调查")
@Table(name="T_CMS_ONLINESURVEY")
public class OnlineSurvey extends BaseObject {
	@InitFieldAnnotation("专题摘要")
	private String summary;	
	@InitFieldAnnotation("开始时间")
	private Date startDate;
	@InitFieldAnnotation("结束时间")
	private List<SurveyOption> surveyOptions=new ArrayList<SurveyOption>();
	public OnlineSurvey() {
    	this.id = StringUtil.generator(); 
    }
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Transient
	public List<SurveyOption> getSurveyOptions() {
		return surveyOptions;
	}
	public void setSurveyOptions(List<SurveyOption> surveyOptions) {
		this.surveyOptions = surveyOptions;
	}
}
