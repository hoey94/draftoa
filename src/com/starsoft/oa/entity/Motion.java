package com.starsoft.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

/**
 * 
 * @Description 议案实体类 议案标题使用tname 提案人使用createId
 * @author 赵一好
 * @date 2016-11-8 下午4:25:06
 * 
 */
@Entity
@Table(name = "T_OA_MOTION")
@InitNameAnnotation("议案表")
public class Motion extends BaseObject {

	@InitFieldAnnotation("整理号/流水号")
	private String zlh;
	@InitFieldAnnotation("立案号")
	private String lah;

	@InitFieldAnnotation("附议人")
	private String fyr;
	@InitFieldAnnotation("提交时间")
	private Date time;
	@InitFieldAnnotation("状态")
	private String status;
	@InitFieldAnnotation("内容")
	private String content;
	@InitFieldAnnotation("议案附件")
	private String url;
	
	@InitFieldAnnotation("部门")
	private String dept;
	
	@InitFieldAnnotation("职位")
	private String duty;
	
	@InitFieldAnnotation("电话")
	private String mobilNumber;
	
	@InitFieldAnnotation("附议人总人数")
	private String fyrNum;
	
	@InitFieldAnnotation("赞成的票数")
	private String agreeNum;
	
	@InitFieldAnnotation("提案人")
	private String motionMan;
	
	public Motion() {
		
		this.id = StringUtil.generator();
		
	}
	
	@Transient
	public String getMotionMan() {
		return motionMan;
	}
	
	public void setMotionMan(String motionMan) {
		this.motionMan = motionMan;
	}


	public String getFyr() {
		return fyr;
	}

	public void setFyr(String fyr) {
		this.fyr = fyr;
	}


	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getZlh() {
		return zlh;
	}

	public void setZlh(String zlh) {
		this.zlh = zlh;
	}

	public String getLah() {
		return lah;
	}

	public void setLah(String lah) {
		this.lah = lah;
	}

	@Transient
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	
	@Transient
	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	@Transient
	public String getMobilNumber() {
		return mobilNumber;
	}

	public void setMobilNumber(String mobilNumber) {
		this.mobilNumber = mobilNumber;
	}

	@Transient
	public String getFyrNum() {
		return fyrNum;
	}

	public void setFyrNum(String fyrNum) {
		this.fyrNum = fyrNum;
	}

	@Transient
	public String getAgreeNum() {
		return agreeNum;
	}

	public void setAgreeNum(String agreeNum) {
		this.agreeNum = agreeNum;
	}

}
