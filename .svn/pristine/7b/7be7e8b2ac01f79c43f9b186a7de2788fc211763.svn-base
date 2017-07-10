package com.starsoft.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

/**
 * 
 * @Description 签批记录表
 * @author 赵一好
 * @date 2016-11-10 下午1:57:47
 * 
 */
@Entity
@Table(name = "T_OA_QIANPIRECORD")
@InitNameAnnotation("签批记录表")
public class QianpiRecord extends BaseObject {

	@InitFieldAnnotation("签批人")
	private String qpr;

	@InitFieldAnnotation("议案")
	private String motionId;

	@InitFieldAnnotation("意见")
	private String content;

	@InitFieldAnnotation("标记附议是否通过1:通过0:未通过")
	private String mark;

	@InitFieldAnnotation("时间")
	private Date time;

	@InitFieldAnnotation("证教处")
	private String zjc;
	
	public QianpiRecord() {
		this.id = StringUtil.generator();
	}

	public String getQpr() {
		return qpr;
	}

	public void setQpr(String qpr) {
		this.qpr = qpr;
	}

	public String getMotionId() {
		return motionId;
	}

	public void setMotionId(String motionId) {
		this.motionId = motionId;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getZjc() {
		return zjc;
	}

	public void setZjc(String zjc) {
		this.zjc = zjc;
	}
	
}
