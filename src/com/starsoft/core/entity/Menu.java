package com.starsoft.core.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("菜单")
@Table(name="T_CORE_MENU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu extends BaseTreeObject {
	@InitFieldAnnotation("链接地址")
	private String url;	
	@InitFieldAnnotation("显示目标")
	private String targets;	
	@InitFieldAnnotation("类型")
	private String menuType;
	@InitFieldAnnotation("菜单图片")
	private String imgurl;
	@InitFieldAnnotation("模块信息")
	private String module;
	
    public Menu() {
    	this.id = StringUtil.generator(); 
    } 
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTargets() {
		return targets;
	}
	public void setTargets(String targets) {
		this.targets = targets;
	}	
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
}
