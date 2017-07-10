package com.starsoft.cms.entity;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("网站导航菜单")
@Table(name="T_CMS_WEBMENU")
public class WebMenu extends BaseTreeObject {
	@InitFieldAnnotation("菜单连接地址")
	private String menuurl;
	public WebMenu() {
    	this.id = StringUtil.generator(); 
    }
	public String getMenuurl() {
		return menuurl;
	}

	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}
}
