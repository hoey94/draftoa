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
 * @Description 附议记录表
 * @author 赵一好
 * @date 2016-11-10 下午1:57:47
 * 
 */
@Entity
@Table(name = "T_OA_FUYIRECORD")
@InitNameAnnotation("附议记录表")
public class FuyiRecord extends BaseObject {

	@InitFieldAnnotation("附议人")
	private String fyr;

	@InitFieldAnnotation("议案")
	private String motionId;

	@InitFieldAnnotation("内容")
	private String content;

	@InitFieldAnnotation("标记附议是否通过1:通过0:未通过")
	private String mark;

	@InitFieldAnnotation("附议的时间")
	private Date time;

	public FuyiRecord() {
		this.id = StringUtil.generator();
	}

	public String getFyr() {
		return fyr;
	}

	public void setFyr(String fyr) {
		this.fyr = fyr;
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

}
