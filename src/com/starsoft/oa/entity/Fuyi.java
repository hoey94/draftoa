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
 * @Description 附议 createId是处理人的id tname是操作的类型
 * @author 赵一好
 * @date 2016-11-9 下午4:04:45
 * 
 */
@Entity
@Table(name = "T_OA_FUYI")
@InitNameAnnotation("操作记录表")
public class Fuyi extends BaseObject {

	@InitFieldAnnotation("附议人")
	private String fyr;
	
	@InitFieldAnnotation("议案")
	private String motionid;

	@InitFieldAnnotation("时间")
	private Date time;

	@InitFieldAnnotation("0:未读1:已读未修改2:已读已修改")
	private String mark;

	public Fuyi() {
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

	public String getFyr() {
		return fyr;
	}

	public void setFyr(String fyr) {
		this.fyr = fyr;
	}
}
