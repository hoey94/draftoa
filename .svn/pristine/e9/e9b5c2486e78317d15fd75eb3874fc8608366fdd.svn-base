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
 * @Description 签批
 * @author 赵一好
 * @date 2016-11-8 下午4:55:31
 * 
 */
@Entity
@Table(name = "T_OA_QIANPI")
@InitNameAnnotation("签批表")
public class Qianpi extends BaseObject {

	@InitFieldAnnotation("签批人")
	private String qpr;
	
	@InitFieldAnnotation("议案")
	private String motionid;

	@InitFieldAnnotation("签批时间")
	private Date time;

	@InitFieldAnnotation("0:未读1:已读未修改2:已读已修改")
	private String mark;

	public Qianpi() {
		this.id = StringUtil.generator();
	}

	public String getMotionid() {
		return motionid;
	}

	public void setMotionid(String motionid) {
		this.motionid = motionid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getQpr() {
		return qpr;
	}

	public void setQpr(String qpr) {
		this.qpr = qpr;
	}
	
}
