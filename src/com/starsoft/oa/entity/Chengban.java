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
 * @Description 承办
 * @author 赵一好
 * @date 2016-11-8 下午5:07:03
 * 
 */
@Entity
@Table(name="T_OA_CHENGBAN")
@InitNameAnnotation("承办表")
public class Chengban extends BaseObject {

	@InitFieldAnnotation("处理人")
	private String clr;

	@InitFieldAnnotation("议案")
	private String motionId;

	@InitFieldAnnotation("内容")
	private String content;
	
	@InitFieldAnnotation("上传的附件")
	private String url;

	@InitFieldAnnotation("承办的时间")
	private Date time;
	
	@InitFieldAnnotation("承办人")
	private String cbr;
	
	@InitFieldAnnotation("状态0:未读1:已读未修改2:已读已修改")
	private String read_index;

	public Chengban() {
		this.id = StringUtil.generator();
	}

	public String getCbr() {
		return cbr;
	}

	public void setCbr(String cbr) {
		this.cbr = cbr;
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

	public String getClr() {
		return clr;
	}

	public void setClr(String clr) {
		this.clr = clr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getRead_index() {
		return read_index;
	}

	public void setRead_index(String read_index) {
		this.read_index = read_index;
	}

}
