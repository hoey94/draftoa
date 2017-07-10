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
 * @Description 立案审批
 * @author 赵一好
 * @date 2016-11-16 上午8:55:51
 *
 */
@Entity
@Table(name = "T_OA_LIANSHENPI")
@InitNameAnnotation("立案审批表")
public class Lianshenpi extends BaseObject{

	@InitFieldAnnotation("议案")
	private String motionId;
	
	@InitFieldAnnotation("立案人")
	private String lar;
	
	@InitFieldAnnotation("时间")
	private Date time;
	
	@InitFieldAnnotation("标记0未通过1通过")
	private String mark;
	
	@InitFieldAnnotation("上传文件")
	private String url;
	
	@InitFieldAnnotation("状态0:未读1:已读未修改2:已读已修改")
	private String read_index;
	
	@InitFieldAnnotation("意见")
	private String content;

	public Lianshenpi(){
		this.id = StringUtil.generator();
	}
	
	public String getMotionId() {
		return motionId;
	}

	public void setMotionId(String motionId) {
		this.motionId = motionId;
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

	public String getLar() {
		return lar;
	}

	public void setLar(String lar) {
		this.lar = lar;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
