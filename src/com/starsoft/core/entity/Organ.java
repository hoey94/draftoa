package com.starsoft.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("学校机构管理")
@Table(name="T_CORE_ORGAN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organ extends BaseTreeObject{
	@InitFieldAnnotation("学校编码")
	private String organCode;
	@InitFieldAnnotation("部门域名")
	private String organDomain;
	@InitFieldAnnotation("部门类型")
	private String organType;
	@InitFieldAnnotation("部门负责人")
	private String leader;
	@InitFieldAnnotation("负责人名称")
	private String leaderName;
	@InitFieldAnnotation("学校封面")
	private String imageurl;
	@InitFieldAnnotation("学校宣传图")
	private String schoolimage;
	@InitFieldAnnotation("校内请求地址")
	private String innerhttp;
	@InitFieldAnnotation("省")
	private String sheng;
	@InitFieldAnnotation("市")
	private String shi;
	@InitFieldAnnotation("更新时间")
	private Date updateTime;
	public Organ() {
    	this.id = StringUtil.generatorShort();
	} 

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	@Column(length=32)
	public String getOrganCode() {
		return organCode;
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}
	@Column(length=32)
	public String getOrganType() {
		return organType;
	}

	public void setOrganType(String organType) {
		this.organType = organType;
	}

	public String getOrganDomain() {
		return organDomain;
	}

	public void setOrganDomain(String organDomain) {
		this.organDomain = organDomain;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getInnerhttp() {
		return innerhttp;
	}

	public void setInnerhttp(String innerhttp) {
		this.innerhttp = innerhttp;
	}
	public String getSchoolimage() {
		return schoolimage;
	}

	public void setSchoolimage(String schoolimage) {
		this.schoolimage = schoolimage;
	}

	public String getSheng() {
		return sheng;
	}

	public void setSheng(String sheng) {
		this.sheng = sheng;
	}

	public String getShi() {
		return shi;
	}

	public void setShi(String shi) {
		this.shi = shi;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
