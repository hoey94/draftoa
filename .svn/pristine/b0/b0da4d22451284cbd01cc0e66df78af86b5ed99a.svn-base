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
 * @Description 立案审批回复
 * @author 赵一好
 * @date 2016-11-16 上午10:50:27
 *
 */
@Entity
@Table(name = "T_OA_LIANSHENPIRECORD")
@InitNameAnnotation("立案审批回复表")
public class LianshenpiRecord extends BaseObject{

	@InitFieldAnnotation("议案")
	private String motionId;
	@InitFieldAnnotation("立案人")
	private String lar;
	@InitFieldAnnotation("处理人")
	private String clr;
	@InitFieldAnnotation("时间")
	private Date time;
	@InitFieldAnnotation("主办方")
	private String zbf;
	@InitFieldAnnotation("协办方")
	private String xbf;
	public LianshenpiRecord(){
		this.id = StringUtil.generator();
	}
	public String getMotionId() {
		return motionId;
	}
	public void setMotionId(String motionId) {
		this.motionId = motionId;
	}
	public String getLar() {
		return lar;
	}
	public void setLar(String lar) {
		this.lar = lar;
	}
	public String getClr() {
		return clr;
	}
	public void setClr(String clr) {
		this.clr = clr;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getZbf() {
		return zbf;
	}
	public void setZbf(String zbf) {
		this.zbf = zbf;
	}
	public String getXbf() {
		return xbf;
	}
	public void setXbf(String xbf) {
		this.xbf = xbf;
	}
}
