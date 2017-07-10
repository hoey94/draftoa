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
 * @Description 承办部门领导回复
 * @author 赵一好
 * @date 2016-11-15 上午8:56:17
 *
 */
@Entity
@Table(name = "T_OA_CHENGBANRECORD")
@InitNameAnnotation("承办领导回复表")
public class ChengbanRecord extends BaseObject{
	@InitFieldAnnotation("议案")
	private String motionId;

	@InitFieldAnnotation("内容")
	private String content;
	
	@InitFieldAnnotation("承办部门")
	private String cbr;

	@InitFieldAnnotation("时间")
	private Date time;
	
	@InitFieldAnnotation("是否通过1:通过0:未通过")
	private String mark;
	
	@InitFieldAnnotation("状态0:未读1:已读未修改2:已读已修改")
	private String read_index;


	public ChengbanRecord(){
		this.id = StringUtil.generator();
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

	public String getCbr() {
		return cbr;
	}

	public void setCbr(String cbr) {
		this.cbr = cbr;
	}

	public String getRead_index() {
		return read_index;
	}

	public void setRead_index(String read_index) {
		this.read_index = read_index;
	}
	
}
