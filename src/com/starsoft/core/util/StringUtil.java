package com.starsoft.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jasper.tagplugins.jstl.core.If;

import com.hp.hpl.sparta.xpath.ThisNodeTest;

public class StringUtil {
	public static int sortCode=10000;
	public static int shortSortCode=0;
	/****
	 * 把字符串更改为Map对象
	 * @param inStr
	 * @param splitstr
	 * @param elesplitstr
	 * @return
	 */
	public static List<Map> toMapValues(String inStr, String splitstr,
			String elesplitstr) {
		ArrayList<Map> resultlist = new ArrayList<Map>();
		if (inStr == null || inStr.equals("")) {
			return null;
		}
		if (inStr.indexOf(splitstr) == -1) {
			return null;
		}
		StringTokenizer str = new StringTokenizer(inStr.trim(), splitstr);
		while (str.hasMoreTokens()) {
			String tmpstr = str.nextToken();
			int i = tmpstr.indexOf(elesplitstr);
			if (i != -1) {
				HashMap map = new HashMap();
				map.put(tmpstr.substring(0, i), tmpstr.substring(i + 1));
				resultlist.add(map);
			}
		}
		return resultlist;
	}
	/**
	 * 字符串转list
	 * @param inStr
	 * @param splitstr
	 * @return
	 */
	public static ArrayList<String> toList(String inStr, String splitstr) {
		ArrayList<String> resulist = new ArrayList<String>();
		if (inStr == null || inStr.equals("")) {
			return resulist;
		}
		if (inStr.indexOf(splitstr) == -1) {
			resulist.add(inStr);
			return resulist;
		}
		StringTokenizer str = new StringTokenizer(inStr.trim(), splitstr);
		while (str.hasMoreTokens()) {
			resulist.add(str.nextToken());
		}
		return resulist;
	}
	public static ArrayList toIntList(String inStr, String splitstr) {
		ArrayList resulist = new ArrayList();
		if (inStr == null || inStr.equals("")) {
			return resulist;
		}
		if (inStr.indexOf(splitstr) == -1) {
			if(isInteger(inStr)){
				resulist.add(Integer.valueOf(inStr));
			}			
			return resulist;
		}
		String strs[]=inStr.split(splitstr);
		for(String str: strs){
			if(isInteger(str)){
				resulist.add(Integer.valueOf(str));
			}
		}
		return resulist;
	}
	public static boolean isInteger(String str) {		
		try{
			Integer.valueOf(str);
			return true;
		}catch(NumberFormatException e){
			return false;
		}		
	}
	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {		
		for(int t=0;t<str.length();t++){
			if(!Character.isDigit(str.charAt(t))){
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断是否为null或者空值
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str){
		return (str==null||"".equals(str.trim()));
	}

	public static Calendar toDate(String qktdatestart) {
		Calendar calendar = Calendar.getInstance();
		String tmpkey = qktdatestart;
		int y = 0, m = 1, d = 1;
		if (tmpkey != null) {
			StringTokenizer st = new StringTokenizer(tmpkey, "-");
			if (st.hasMoreTokens()) {
				y = Integer.parseInt(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				m = Integer.parseInt(st.nextToken()) - 1;
			}
			if (st.hasMoreTokens()) {
				d = Integer.parseInt(st.nextToken());
			}
			calendar.set(y, m, d, 0, 0, 0);
		}
		return calendar;
	}
	 /**
     * Get float parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static String[] getArray(String str,String splitstr) {
        if(str==null||str.equals("")){
	   		return null ;
	   	}
        String[] result=str.split(splitstr);
        return result;
    }
    /***
	 * 获取UUID字符串
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = java.util.UUID.randomUUID();
	   	return uuid.toString().replaceAll("-", "");
	}
    /***
	 * 获取年月日时分秒毫秒等共32位字符串
	 * @return
	 */
	public static String generator(){
		 StringBuffer now = new StringBuffer(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		 if(sortCode>99990){
			 sortCode=10000;
		 }else{
			 sortCode++;
		 }
		 int b = (int)(Math.random() * 90000.0D + 10000.0D);
		 int c = (int)(Math.random() * 90000.0D + 10000.0D);
		 return (now.append(sortCode).append(b).append(c)).toString();
	}
    /***
	 * 获取年月日时分秒毫秒等共17位字符串
	 * @return
	 */
	public static String generatorShort(){
		 StringBuffer now = new StringBuffer(new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()));
		 if(shortSortCode>99){
			 shortSortCode=0;
		 }else{
			 shortSortCode++;
		 }
		 if(shortSortCode>9){
			 return (now.append(shortSortCode)).toString();
		 }else{
			 return (now.append(0).append(shortSortCode)).toString(); 
		 }
	}
	public static String getSFirstDaxie(String str){
		String result=str.trim();
		if(result.length()<1){
			return "";
		}else if(result.length()==1){
			return result.substring(0, 1).toUpperCase();
		}else{
			result=result.substring(0, 1).toUpperCase()+result.substring(1, result.length());
			return result;
		}
	}
	/***
	 * 是否是手机号码
	 * @param number
	 * @return
	 */
	public static boolean isMobileNumber(String number) {
		if(number.startsWith("86")){
			number=number.replaceFirst("86", "");
		}
		if(number.length()!=11) return false;
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$"); 	
		Matcher m = p.matcher(number);  
		return m.matches();
	}
	/***
	 * 判断是否为邮箱格式
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"); 	
		Matcher m = p.matcher(email);  
		return m.matches();
	}

	/**
	 * @Deprecated 通过list转成逗号分割的字符串
	 * @Date 2016年9月22日
	 * @param params
	 * @return
	 */
	public static String getStringByList(List<String> params)
	{
		StringBuilder studentIdStr = new StringBuilder();
		for (String studentId : params)
		{
			studentIdStr.append("'" + studentId + "',");
		}
		String result = "";
		if (studentIdStr.length() > 0)
		{
			result = studentIdStr.toString().substring(0, studentIdStr.length()-1);
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			List<String> studentIds = new ArrayList<String>();
			studentIds.add("lizixian");
			studentIds.add("6666666666");
			System.out.println(StringUtil.getStringByList(studentIds));
	}
}