package com.starsoft.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
@Entity
@InitNameAnnotation("评论管理")
@Table(name="T_CMS_COMMENT")
public class Comment extends BaseObject {
	@InitFieldAnnotation("评论关联标识")
	private String commentObjectId;
	@InitFieldAnnotation("评论内容")
	private String commentContent;
	public String getCommentObjectId() {
		return commentObjectId;
	}
	public void setCommentObjectId(String commentObjectId) {
		this.commentObjectId = commentObjectId;
	}
	@Column(length=1024)
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
}
