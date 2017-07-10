package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.DateUtil;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

/***
 * 访问量类
 * @author Administrator
 *
 */
@Entity
@InitNameAnnotation("访问量类")
@Table(name="T_CMS_VISTERNUMBER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VisterNumber extends BaseObject {
	@InitFieldAnnotation("总访问量")
	private Integer totolNumber;
	@InitFieldAnnotation("今年访问量")
	private Integer yearNumber;
	@InitFieldAnnotation("昨天访问量")
	private Integer yesterdayNumber;
	@InitFieldAnnotation("今天访问量")
	private Integer todayNumber;
	public VisterNumber() {
    	this.id = StringUtil.generator();
    	this.totolNumber=0;
    	this.yearNumber=0;
    	this.yesterdayNumber=0;
    	this.todayNumber=0;
    }
	public Integer getTotolNumber() {
		return totolNumber;
	}
	public void setTotolNumber(Integer totolNumber) {
		this.totolNumber = totolNumber;
	}
	public Integer getYearNumber() {
		return yearNumber;
	}
	public void setYearNumber(Integer yearNumber) {
		this.yearNumber = yearNumber;
	}
	public Integer getYesterdayNumber() {
		return yesterdayNumber;
	}
	public void setYesterdayNumber(Integer yesterdayNumber) {
		this.yesterdayNumber = yesterdayNumber;
	}
	public Integer getTodayNumber() {
		return todayNumber;
	}
	public void setTodayNumber(Integer todayNumber) {
		this.todayNumber = todayNumber;
	}
	public void addVisterHistory() {
		String date=DateUtil.getCurDate();
		String yearsday=DateUtil.formatDate(DateUtil.previous(1));
		if(date.equals(this.tname)){//今天日期
			this.setTodayNumber(this.getTodayNumber()+1);
			this.setTotolNumber(this.getTotolNumber()+1);
		}else if(yearsday.equals(this.tname)){
			this.setYesterdayNumber(this.getTodayNumber());
			this.setTodayNumber(1);
			this.setTotolNumber(this.getTotolNumber()+1);
			this.setTname(date);
		}else{
			this.setTodayNumber(1);
			this.setYesterdayNumber(0);
			this.setTname(date);
			this.setTotolNumber(this.getTotolNumber()+1);
		}
		
	}
}
