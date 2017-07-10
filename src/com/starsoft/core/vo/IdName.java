package com.starsoft.core.vo;

/***
 * 系统内容
 * @author Administrator
 *
 */
public class IdName {
	private String id; 
	private String tname;
	public IdName(String id, String tname) {
		super();
		this.id = id;
		this.tname = tname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	
}
