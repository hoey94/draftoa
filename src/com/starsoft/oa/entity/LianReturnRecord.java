package com.starsoft.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@Table(name="T_OA_LIANRETURNRECORD")
@InitNameAnnotation("立案回复记录表")
public class LianReturnRecord extends BaseObject{

	@InitFieldAnnotation("立案人")
	private String lar;
	
	@InitFieldAnnotation("时间")
	private Date time;
	
	@InitFieldAnnotation("议案")
	private String motionId;
	
	@InitFieldAnnotation("1:同意0不同意")
	private String mark;
	
	@InitFieldAnnotation("内容")
	private String content;

	public LianReturnRecord(){
		this.id = StringUtil.generator();
	}
	
	public String getLar() {
		return lar;
	}

	public void setLar(String lar) {
		this.lar = lar;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getMotionId() {
		return motionId;
	}

	public void setMotionId(String motionId) {
		this.motionId = motionId;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
