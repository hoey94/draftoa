package com.starsoft.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("个人消息中心")
@Table(name="T_CORE_MSGINFO")
public class MsgInfo extends BaseObject{//createId作为发送人ID
	@InitFieldAnnotation("接收用户")
	private String receiverId;
	@InitFieldAnnotation("关联信息标识")//大的消息内容要关联
	private String baseObjectId;
	@InitFieldAnnotation("消息类别")
	private String msgInfoType;
	public MsgInfo(){
		this.id=StringUtil.generator();
		this.setValid(true);//待读
	}
	
	public String getBaseObjectId() {
		return baseObjectId;
	}

	public void setBaseObjectId(String baseObjectId) {
		this.baseObjectId = baseObjectId;
	}

	public String getMsgInfoType() {
		return msgInfoType;
	}
	public void setMsgInfoType(String msgInfoType) {
		this.msgInfoType = msgInfoType;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	
}
