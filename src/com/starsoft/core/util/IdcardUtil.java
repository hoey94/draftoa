package com.starsoft.core.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，
 * 三位数字顺序码和一位数字校验码。 </br> 2、地址码(前六位数）
 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。</br>
 * 
 * 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。</br>
 * 
 * 4、顺序码（第十五位至十七位）
 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。</br>
 * 
 * 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16
 * ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7
 * 9 10 5 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10
 * 校验码: 1 0 X 9 8 7 6 5 4 3 2
 * 
 * */
public class IdcardUtil {

	private static Log log = LogFactory.getLog(IdcardUtil.class);

	/**
	 * ======================================================================
	 * 功能：身份证的有效验证
	 * 
	 * @param IDStr
	 *            身份证号
	 * @return 有效：true 无效：false
	 * @throws ParseException
	 */
	public static boolean validatIDCard(String IDStr) {
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		// String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
		String Ai = "";

		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			if (log.isWarnEnabled()) {
				log.warn("号码长度应该为15位或18位。");
			}
			return false;
		}

		// ================ 数字 除最后一位都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			if (log.isWarnEnabled()) {
				log.warn("15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。");
			}
			return false;
		}

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份

		if (DateUtil.isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			if(log.isWarnEnabled()){
				log.warn("生日无效。");
			}
			return false;
		}

		GregorianCalendar gc = new GregorianCalendar();
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
				|| (gc.getTime().getTime() - DateUtil.parseDate(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
			if(log.isWarnEnabled()){
				log.warn("生日不在有效范围。");
			}
			return false;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			if(log.isWarnEnabled()){
				log.warn("月份无效");
			}
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			if(log.isWarnEnabled()){
				log.warn("日期无效");
			}
			return false;
		}


		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				if(log.isWarnEnabled()){
					log.warn("身份证无效，最后一位字母错误");
				}
				return false;
			}
		} else {
			return true;
		}
		return true;
	}


	/**
	 * 根据身份证号，取出生日期
	 * @param idcardCode
	 * @return
	 */
	public static Date getCsrq(String idcardCode){
		String dateString = "";
		if(validatIDCard(idcardCode)){
			if(idcardCode.length() == 15){
				dateString = "19" + idcardCode.substring(6,8) + "-" + idcardCode.substring(8, 10) + "-" + idcardCode.substring(10, 12);
			}else if(idcardCode.length() == 18){
				dateString = idcardCode.substring(6,10) + "-" + idcardCode.substring(10,12) + "-" + idcardCode.substring(12, 14);
			}
			return DateUtil.parseDate(dateString);
		}
		return null;
	}
	
	/**
	 * 出生年
	 * @param idcardCode
	 * @return
	 */
	public static String getCsn(String idcardCode){
		String csn = null;
		if(validatIDCard(idcardCode)){
			if(idcardCode.length() == 15){
				csn = "19" + idcardCode.substring(6,8);
			}else if(idcardCode.length() == 18){
				csn = idcardCode.substring(6, 10);
			}
		}
		return csn;
	}
	
	/**
	 * 出生月
	 * @param idcardCode
	 * @return
	 */
	public static String getCsy(String idcardCode){
		String csn = null;
		if(validatIDCard(idcardCode)){
			if(idcardCode.length() == 15){
				csn = idcardCode.substring(8,10);
			}else if(idcardCode.length() == 18){
				csn = idcardCode.substring(10, 12);
			}
		}
		return csn;
	}
	
	/**
	 * 出生日
	 * @param idcardCode
	 * @return
	 */
	public static String getCsr(String idcardCode){
		String csn = null;
		if(validatIDCard(idcardCode)){
			if(idcardCode.length() == 15){
				csn = idcardCode.substring(10,12);
			}else if(idcardCode.length() == 18){
				csn = idcardCode.substring(12, 14);
			}
		}
		return csn;
	}
	
	/**
	 * ======================================================================
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// String IDCardNum="210102820826411";
		// String IDCardNum="210102198208264114";
		String IDCardNum = "130185198811281323";
		System.out.println(IdcardUtil.getCsy(IDCardNum));
		// System.out.println(cc.isDate("1996-02-29"));
	}
}
