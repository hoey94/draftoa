package com.starsoft.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.starsoft.core.util.DateUtil;

public class DateUtil {
	public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static String DATE_FORMAT = "yyyy-MM-dd";
	public static String TIME_FORMAT="yyyy/MM/dd/HH/mm/ss";
	public static String FULL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	//紧凑型日期格式，也就是纯数字类型yyyyMMdd
	public static final DateUtil COMPAT = new DateUtil(new SimpleDateFormat(
			"yyyyMMdd"));
	// 常用日期格式，yyyy-MM-dd
	public static final DateUtil COMMON = new DateUtil(new SimpleDateFormat(
			"yyyy-MM-dd"));
	public static final DateUtil COMMON_FULL = new DateUtil(
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	private final SimpleDateFormat format;

	public DateUtil(SimpleDateFormat format) {
		this.format = format;
	}
	public SimpleDateFormat getFormat() {
		return format;
	}
	/**
	 * 日期获取字符串
	 */
	public String getDateText(Date date) {
		return getFormat().format(date);
	}
	/**
	 * 字符串获取日期
	 * @throws ParseException 
	 */
	public Date getTextDate(String text) throws ParseException{
		return getFormat().parse(text);
	}
	/**
	 * Get the previous time, from how many days to now.
	 * 
	 * @param days
	 *            How many days.
	 * @return The new previous time.
	 */
	public static Date previous(int days) {
		return new Date(System.currentTimeMillis() - days * 3600000L * 24L);
	}
	/***
	 * 得到多少天以后的时间
	 * @param days
	 * @return
	 */
	public static Date after(int days) {
		return new Date(System.currentTimeMillis() + days * 3600000L * 24L);
	}
	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(Date d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(long d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * Convert date to String like "yyyy-MM-dd".
	 */
	public static String formatDate(Date d) {
		return new SimpleDateFormat(DATE_FORMAT).format(d);
	}

	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseDate(String d) {
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(d);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Parse date and time like "yyyy-MM-dd hh:mm".
	 */
	public static Date parseDateTime(String dt) {
		try {
			return new SimpleDateFormat(DATETIME_FORMAT).parse(dt);
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm:ss".
	 */
	public static Date parseFullDateTime(String date) {
		try {
			return new SimpleDateFormat(FULL_DATETIME_FORMAT).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	 /***
     * 取得当前时间
     * 格式 yyyy-MM-dd HH:mm
     * @return
     */
    public static String getCurDateTime(){
    	return formatDateTime(new Date());
    }
    /***
     * 取得当前时间
     * 格式 yyyy/MM/dd/HH/mm/ss
     * @return
     */
    public static String getCurDateTimeSend(){
    	return new SimpleDateFormat(TIME_FORMAT).format(new Date());
    }
    /**
     * 获取当前服务器年数
     * @return
     */
    public static String getCurYear(){
    	String date=formatDateTime(new Date());
    	String year=date.substring(0, date.indexOf("-"));
    	return year;
    }
    /**
     * 获取当前服务器年数
     * @return
     */
    public static String getCurDate(){
    	return new SimpleDateFormat(DATE_FORMAT).format(new Date());
    }
    /**
    * 得到星期几
    * @param date 日期
    * @return
    */
    public static String getWeekOfDate(Date date) {
	     String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	     Calendar cal = Calendar.getInstance();
	     cal.setTime(date);
	     int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	     if (w < 0)
	         w = 0;
	     return weekDays[w];
     }
    /**
     * 得到一周的第几天
     * @param date 日期
     * @return
     */
     public static int getWeeksOfDate(Date date) {
 	     Calendar cal = Calendar.getInstance();
 	     cal.setTime(date);
 	     int w = cal.get(Calendar.DAY_OF_WEEK);
 	     return w;
      }
     /**
      * 得到一个月份的天数
      * @param date
      * @return
      */
     public static int getDaysOfMonth(Date date){
    	 Calendar cal = Calendar.getInstance();
 	     cal.setTime(date);
 	     int w = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
 	     return w;
     }
     /**
      * 得到一个月份的周数
      * @param date
      * @return
      */
     public static int getWeeksOfMonth(Date date){
    	 Calendar cal = Calendar.getInstance();
 	     cal.setTime(date);
 	     int w = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
 	     return w;
     }
    /**
     * 返回一年后的时间
     * @param date
     * @return
     */
    public static String getNextYear(Date date){
    	long beforeTime=(date.getTime()/1000)+60*60*24*365;
    	Date nextYear=new Date();
    	nextYear.setTime(beforeTime*1000);
    	return formatDate(nextYear);
    }
    /**
     * 返回上个月的今天
     * @return
     */
    public static String getNowOfLastMonth() {
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar aGregorianCalendar = new GregorianCalendar();
        aGregorianCalendar.set(Calendar.MONTH, aGregorianCalendar
                .get(Calendar.MONTH) - 1);
        String nowOfLastMonth = aSimpleDateFormat
                .format(aGregorianCalendar.getTime());
        return nowOfLastMonth;
    }

    /***
     * 
     * @return
     */
    public static int getCompareToday(Date date,Date date2){
        Long result = date2.getTime() / 86400000 - date.getTime() / 86400000;  //用立即数，减少乘法计算的开销
        int t=Long.numberOfLeadingZeros(result);
    	return t;
    }
    /**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatFullDateTime(Date d) {
		return new SimpleDateFormat(FULL_DATETIME_FORMAT).format(d);
	}
	/**
     * 返回本月的第一天
     * @return
     */
    public static String getFirstDayOfMonth() {
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar aGregorianCalendar = new GregorianCalendar();
        aGregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        String nowOfLastMonth = aSimpleDateFormat
                .format(aGregorianCalendar.getTime());
        return nowOfLastMonth;
    }
    /**
     * 返回本月的第一天
     * @return
     */
    public static String getLastDayOfMonth(String currday) {
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar aGregorianCalendar = new GregorianCalendar();
        aGregorianCalendar.set(Calendar.DAY_OF_MONTH, aGregorianCalendar.getMaximum(Calendar.DAY_OF_MONTH));
        String nowOfLastMonth = aSimpleDateFormat
                .format(aGregorianCalendar.getTime());
        return nowOfLastMonth;
    }
    public static void main(String[] args) throws Exception {
    	Date date=parseDate("2011-06-12");
    	int test=getDaysOfMonth(date);
    	System.out.println(test);
    }
    public static boolean isDate(String str) {		
		try{
			new SimpleDateFormat(DATE_FORMAT).parse(str);
			return true;
		}catch(ParseException e){
			return false;
		}		
	}
    public static boolean isDateTime(String str) {		
		try{
			new SimpleDateFormat(FULL_DATETIME_FORMAT).parse(str);
			return true;
		}catch(ParseException e){
			return false;
		}		
	}
    
    public static Date getStartDate(String currday){
    	Date result=DateUtil.parseDate(currday);
		result.setHours(0);
		result.setMinutes(0);
		result.setSeconds(0);
		return result;
    }
    public static Date getEndDate(String currday){
    	Date result=DateUtil.parseDate(currday);
    	result.setHours(23);
		result.setMinutes(59);
		result.setSeconds(59);
		return result;
    }
    /**
   	 * 日期获取字符串
   	 */
   	public static String getDateText(Date date ,String format){
   		return new SimpleDateFormat(format).format(date);
   	}
   	
}
