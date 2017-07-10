package com.starsoft.core.util;

import java.util.HashMap;
import java.util.Map;
/***
 * 静态变量
 * @author lenovo
 *
 */
public class WEBCONSTANTS {
	public static final String HOST_URL = "http://www.2edus.com";
	public static final String SESSION_USER = "SESSONUSER";
	public static final String SESSION_USERNAME = "loginUserName";
	public static final String SESSION_USERID = "loginUserId";
	public static final String SESSION_ROLEIDS = "loginRoleIds";
	public static final String SESSION_ROLEFLAG="roleflag";
	public static final String SESSION_USERTYPE="USERTYPE";
	
	public static final String SUCCESSINFOR ="操作成功！";
	public static final String FAILINFOR ="操作失败！";
	public static final String WELCOME = "祝贺你成功注册成为管理系统企业级用户！";
	public static final String SESSION_ORGANIZATION = "ORGANIZATION";
	public static final String SESSION_ORGANID = "ORGANID";
	public static final String Education_Cloud ="10000";

	/**
	 * 密码在做SHA512摘要时加入的混淆码
	 * 此码在任何情况下都不能改动，不然会造成原有的用户都不能登录
	 */
	public static final String PASSWORD_SALT = "d703bb6b21799c3487a8b778959643eec3ae4405ee3621d4cb651e64f0082e440de897193b356770e0df0528beee6cddad1d408ca82d1dc3869afbe1a9bcd475";
	/**
	 * 是否第一次访问
	 */
	public static boolean firstvisit=true;
	/**
	 * 链接类型是否更改
	 */
	public static boolean linkcontentupdatestate = false;
	/**
	 * 文章类型是否更改
	 */
	public static boolean articleupdatestate=false;
	/**
	 * 文章类型是否更改
	 */
	public static boolean advertisementupdatestate=false;
	/***
	 * 是否为哀悼日
	 */
	public static boolean webcondolence=false;
	/**
	 * 是否为喜庆之日
	 */
	public static boolean webcelebrate=false;
	/***
	 * 导航类型更新
	 */
	/**
	 * 数值类型
	 */
	public static String ZERO_NUM = "0";
	/**
	 * 数值类型
	 */
	public static String ONE_NUM = "1";
	/**
	 * 数值类型
	 */
	public static String TWO_NUM = "2";
	/**
	 * 数值类型
	 */ 
	public static String THREE_NUM = "3";
	/**
	 * 数值类型
	 */
	public static String FOUR_NUM = "4";
	public static Map commonMap=new HashMap();
	@SuppressWarnings("unchecked")
	private static HashMap systemPropertyMap = new HashMap();
	//工作流相关配置
	public static final String OPERAUTHORITY="OPERAUTHORITY";
	public static final String READAUTHORITY="READAUTHORITY";
	public static final String NOAUTHORITY="NOAUTHORITY";
	
	/**
	 * 获得对应的配置项的value
	 * 
	 * @param id
	 *            Long
	 * @return String
	 */
	public static String getSystemProperty(String id) {
		String value = null;
		if (id != null) {
			// 获取配置项值
			value = (String) systemPropertyMap.get(id);

			if (value != null) {
				// 返回结果
				return value;
			} else {
				// 无此配置项值返回为空
				return "";
			}
		} else {
			// 返回为空
			return "";
		}
	}
	/**
	 * 获得对应的配置项的value
	 * 
	 * @param id
	 *            Long
	 * @return String
	 */
	public static void setSystemProperty(String key,String value) {
		if (key != null) {
			systemPropertyMap.put(key, value);
		}
	}
}
