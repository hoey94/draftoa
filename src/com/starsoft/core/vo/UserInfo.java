package com.starsoft.core.vo;
/***
 * 用户、通讯录等封装实体类，用于手机端之间交互
 * @author Administrator
 *
 */
public class UserInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8678205923099001323L;
	private String id; // 成员ID
	private String title;// 头衔 如：部门经理
	private String tname; // 成员姓名 例如：张三丰
	private String password; // 成员密码
	private String mobile; // 手机号码
	private String icon; // 成员头像
	private String sex;// 男1 女0
	private String pinyin;//拼音
	private String organId;//部门标识
	private String organName;//部门名称
	private String userType;//用户类型
	public UserInfo(){
		
	}
	public UserInfo(String id,String tname,String password,String mobile,String organId,String organName,String sex,String userType){
		this.setId(id);
		this.setTname(tname);
		this.setPassword(password);
		this.setMobile(mobile);
		this.setOrganId(organId);
		this.setOrganName(organName);
		this.setUserType(userType);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
