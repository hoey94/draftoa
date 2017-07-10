package com.starsoft.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;
@Entity
@InitNameAnnotation("图片播放器")
@Table(name="T_CMS_IMAGEPLAY")
public class ImagePlay extends BaseObject {
	@InitFieldAnnotation("播放器代码")
	private String playCode;
	@InitFieldAnnotation("宽度")
	private Integer width;
	@InitFieldAnnotation("高度")
	private Integer height;
	@InitFieldAnnotation("播放器类型")
	private String playType;
	@InitFieldAnnotation("图片来源类型")
	private String imageSource;
	@InitFieldAnnotation("关联栏目")
	private String columnId;
	@InitFieldAnnotation("显示图片数量")
	private Integer maxImageNum;
	public ImagePlay() {
    	this.id = StringUtil.generator(); 
    }
	public String getPlayCode() {
		return playCode;
	}
	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public String getImageSource() {
		return imageSource;
	}
	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public Integer getMaxImageNum() {
		return maxImageNum;
	}
	public void setMaxImageNum(Integer maxImageNum) {
		this.maxImageNum = maxImageNum;
	}
}
