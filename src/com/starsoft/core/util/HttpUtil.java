package com.starsoft.core.util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;

import com.starsoft.core.util.StringUtil;
import com.starsoft.core.entity.Users;
public final class HttpUtil {
	/***
	 * 回去请求地址
	 * @param request
	 * @return
	 */
    public static String getURL(HttpServletRequest request) {
        StringBuffer sb = request.getRequestURL();
        String queryString = request.getQueryString();
        if(queryString!=null)
            return sb.toString() + "?" + queryString;
        return sb.toString();
    }
    /**
     * Get Integer parameter from request. If specified parameter name 
     * is not found, the default value will be returned.
     */
    public static int getInt(HttpServletRequest request, String paramName, int defaultValue) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            return defaultValue;
        return Integer.parseInt(s);
    }

    /**
     * Get Integer parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static int getInt(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Integer.parseInt(s);
    }

    /**
     * Get String parameter from request. If specified parameter name 
     * is not found, the default value will be returned.
     */
    public static String getString(HttpServletRequest request, String paramName, String defaultValue) {
    	try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            return defaultValue.trim();
        return s.trim();
    }
    public static String getAttributeValue(HttpServletRequest request, String paramName, String defaultValue) {
        Object result = request.getAttribute(paramName);
        if(result!=null){
        	return String.valueOf(result);
        }else{
        	return defaultValue;
        }
    }
    /**
     * Get String parameter from request. If specified parameter name 
     * is not found or empty, an Exception will be thrown.
     */
    public static String getString(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            throw new NullPointerException("Null parameter: " + paramName);
        return s.trim();
    }

    /**
     * Get Boolean parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static boolean getBoolean(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Boolean.parseBoolean(s);
    }

    /**
     * Get Boolean parameter from request. If specified parameter name 
     * is not found, the default value will be returned.
     */
    public static boolean getBoolean(HttpServletRequest request, String paramName, boolean defaultValue) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            return defaultValue;
        return Boolean.parseBoolean(s);
    }

    /**
     * Get float parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static float getFloat(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Float.parseFloat(s);
    }
    
    /**
     * Get float parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static List<String> getList(HttpServletRequest request, String paramName,String splitstr) {
        String str = request.getParameter(paramName);
        if(str==null||str.equals("")){
	   		return new ArrayList<String>();
	   	}
        return StringUtil.toList(str, splitstr);
    }
    public static String htmlEncode(String text){
        if(text==null || "".equals(text))
            return "";
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace(" ", "&nbsp;");
        text = text.replace("\"", "&quot;");
        text = text.replace("\'", "&apos;");
        return text.replace("\n", "<br/>");
    }
    
    /**
     * Get float parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static String[] getArray(HttpServletRequest request, String paramName,String splitstr) {
        String str = request.getParameter(paramName);
        if(str==null||str.equals("")){
	   		return null ;
	   	}
        String[] result=str.split(splitstr);
        return result;
    }
    /**
     * 获取时间类型参数
     * @param request
     * @param paramName
     * @param defaultvalue
     * @return
     */
    public static Date getDate(HttpServletRequest request, String paramName,String defaultvalue){
    	Date result=null;
		String str = request.getParameter(paramName);
		if (str == null || str.equals("")) {
			return null;
		} else if (str.length() == 10) {
			result=DateUtil.parseDate(str);
		}else if(str.length() == 10){
			result=DateUtil.parseDateTime(str);
		}
		return result;
    }
    /***
     * 从session中获取对象值
     * @param request
     * @param paramName
     * @return
     */
    public static String getStringFromSession(HttpServletRequest request, String paramName) {
        String result=(String)WebUtils.getSessionAttribute(request,paramName);
        if(result==null || result.equals("")){
        	return "";
        }
        return result;
    }
    /***
	 * 分页信息抽取
	 * @param request
	 * @return
	 */
	public static Page convertPage(HttpServletRequest request){
		String result=(String)WebUtils.getSessionAttribute(request,"system.pageSize");
		int pageindex=HttpUtil.getInt(request, "page",1);
		int pageSize=15;
		if(result!=null&&!result.equals("")){
			pageSize=Integer.valueOf(result);
		}
		int rows=HttpUtil.getInt(request, "rows",pageSize);
		return new Page(pageindex,rows);
	}
	 /***
     * 获取所有的参数
     * @param request
     * @return
     */
    public static Map<String,Object> convertModel(HttpServletRequest request){
    	Map<String,Object> model = new HashMap<String,Object>();
		Enumeration<String> names=request.getParameterNames();
		while(names.hasMoreElements()){
			String key=names.nextElement();
			Object value=request.getParameter(key);
			model.put(key, value);
		}
		return model;
    }
	/**
	 * 根据useragent判断是否是手机访问
	 * @param userAgent
	 * @return
	 */
	public static boolean isMobile(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null)
			userAgent = "";
		boolean ismobile = false;
		if (userAgent.indexOf("Android") > -1 || userAgent.indexOf("Eric") > -1
				|| // Ericsson WAP phones and emulators
				userAgent.indexOf("WapI") > -1 || // Ericsson WapIDE 2.0
				userAgent.indexOf("MC21") > -1 || // Ericsson MC218
				userAgent.indexOf("AUR") > -1 || // Ericsson R320
				userAgent.indexOf("R380") > -1 || // Ericsson R380
				userAgent.indexOf("UP.B") > -1 || // UP.Browser
				userAgent.indexOf("WinW") > -1 || // WinWAP browser
				userAgent.indexOf("UPG1") > -1 || // UP.SDK 4.0
				userAgent.indexOf("upsi") > -1 || // another kind of UP.Browser
				userAgent.indexOf("QWAP") > -1 || // unknown QWAPPER browser
				userAgent.indexOf("Jigs") > -1 || // unknown JigSaw browser
				userAgent.indexOf("Java") > -1 || // unknown Java based browser
				userAgent.indexOf("Alca") > -1 || // unknown Alcatel-BE3 browser
													// (UP based)
				userAgent.indexOf("MITS") > -1 || // unknown Mitsubishi browser
				userAgent.indexOf("MOT-") > -1 || // unknown browser (UP based)
				userAgent.indexOf("My S") > -1 || // unknown Ericsson devkit
													// browser
				userAgent.indexOf("WAPJ") > -1 || // Virtual WAPJAG
													// www.wapjag.de
				userAgent.indexOf("fetc") > -1 || // fetchpage.cgi Perl script
													// from www.wapcab.de
				userAgent.indexOf("ALAV") > -1 || // yet another unknown UP
													// based browser
				userAgent.indexOf("Wapa") > -1 || // another unknown browser
													// (Web based "Wapalyzer")
				userAgent.indexOf("UCWEB") > -1 || // another unknown browser
													// (Web based "Wapalyzer")
				userAgent.indexOf("BlackBerry") > -1 || // another unknown
														// browser (Web based
														// "Wapalyzer")
				userAgent.indexOf("J2ME") > -1 || // another unknown browser
													// (Web based "Wapalyzer")
				userAgent.indexOf("Oper") > -1 || // 这个是iphone的
//				userAgent.indexOf("Safari") > -1 || // 这个是iphone的
				userAgent.indexOf("iPhone") > -1 || // 这个是iphone的
				userAgent.indexOf("Noki") > -1) // Nokia phones and emulators
		{
			ismobile = true;
		}
		return ismobile;
	}
	public static String getBrowserType(HttpServletRequest request){
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null){
			userAgent="";
		}
		if (userAgent.contains("Windows")) {//主流应用靠前
			/**
			 * ******************
			 * 台式机 Windows 系列
			 * ******************
			 * Windows NT 6.2	-	Windows 8
			 * Windows NT 6.1	-	Windows 7
			 * Windows NT 6.0	-	Windows Vista
			 * Windows NT 5.2	-	Windows Server 2003; Windows XP x64 Edition
			 * Windows NT 5.1	-	Windows XP
			 * Windows NT 5.01	-	Windows 2000, Service Pack 1 (SP1)
			 * Windows NT 5.0	-	Windows 2000
			 * Windows NT 4.0	-	Microsoft Windows NT 4.0
			 * Windows 98; Win 9x 4.90	-	Windows Millennium Edition (Windows Me)
			 * Windows 98	-	Windows 98
			 * Windows 95	-	Windows 95
			 * Windows CE	-	Windows CE
			 * 判断依据:http://msdn.microsoft.com/en-us/library/ms537503(v=vs.85).aspx
			 */
			if (userAgent.contains("Windows NT 6.3")) {//Windows 2012
				return judgeBrowser(userAgent, "Windows", "10");//判断浏览器
			} else if (userAgent.contains("Windows NT 6.2")) {//Windows 8
				return judgeBrowser(userAgent, "Windows", "8");//判断浏览器
			} else if (userAgent.contains("Windows NT 6.1")) {//Windows 7
				return judgeBrowser(userAgent, "Windows", "7");
			} else if (userAgent.contains("Windows NT 6.0")) {//Windows Vista
				return judgeBrowser(userAgent, "Windows", "Vista");
			} else if (userAgent.contains("Windows NT 5.2")) {//Windows XP x64 Edition
				return judgeBrowser(userAgent, "Windows", "XP");
			} else if (userAgent.contains("Windows NT 5.1")) {//Windows XP
				return judgeBrowser(userAgent, "Windows", "XP");
			} else if (userAgent.contains("Windows NT 5.01")) {//Windows 2000, Service Pack 1 (SP1)
				return judgeBrowser(userAgent, "Windows", "2000");
			} else if (userAgent.contains("Windows NT 5.0")) {//Windows 2000
				return judgeBrowser(userAgent, "Windows", "2000");
			} else if (userAgent.contains("Windows NT 4.0")) {//Microsoft Windows NT 4.0
				return judgeBrowser(userAgent, "Windows", "NT 4.0");
			} else if (userAgent.contains("Windows 98; Win 9x 4.90")) {//Windows Millennium Edition (Windows Me)
				return judgeBrowser(userAgent, "Windows", "ME");
			} else if (userAgent.contains("Windows 98")) {//Windows 98
				return judgeBrowser(userAgent, "Windows", "98");
			} else if (userAgent.contains("Windows 95")) {//Windows 95
				return judgeBrowser(userAgent, "Windows", "95");
			} else if (userAgent.contains("Windows CE")) {//Windows CE
				return judgeBrowser(userAgent, "Windows", "CE");
			} 
		} else if (userAgent.contains("Mac OS X")) {
			/**
			 * ********
			 * 苹果系列
			 * ********
			 * iPod	-		Mozilla/5.0 (iPod; U; CPU iPhone OS 4_3_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8G4 Safari/6533.18.5
			 * iPad	-		Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10
			 * iPad2	-		Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X; en-us) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B176 Safari/7534.48.3
			 * iPhone 4	-	Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7
			 * iPhone 5	-	Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3
			 * 判断依据:http://www.useragentstring.com/pages/Safari/
			 * 参考:http://stackoverflow.com/questions/7825873/what-is-the-ios-5-0-user-agent-string
			 * 参考:http://stackoverflow.com/questions/3105555/what-is-the-iphone-4-user-agent
			 */
			if (userAgent.contains("iPod")) {
				return judgeBrowser(userAgent, "iPod", null);//判断浏览器
			}
		}else if(userAgent.contains("Android")){
			return judgeBrowser(userAgent, "Android", null);//判断安卓客户端浏览器
		}
		return userAgent;
	}
	/**
	 * 用途：根据客户端 User Agent Strings 判断其浏览器
	 * if 判断的先后次序：
	 * 根据浏览器的用户使用量降序排列，这样对于大多数用户来说可以少判断几次即可拿到结果：
	 * 	>>Browser:Chrome > FF > IE > ...
	 * @param userAgent:user agent
	 * @param platformType:平台
	 * @param platformSeries:系列
	 * @param platformVersion:版本
	 * @return
	 */
	private static String judgeBrowser(String userAgent, String platformType, String platformSeries) {
		if (userAgent.contains("Chrome")) {
			/**
			 * ***********
			 * Chrome 系列
			 * ***********
			 * Chrome 24.0.1295.0	-	Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.15 (KHTML, like Gecko) Chrome/24.0.1295.0 Safari/537.15
			 * Chrome 24.0.1292.0	-	Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.14 (KHTML, like Gecko) Chrome/24.0.1292.0 Safari/537.14
			 * Chrome 24.0.1290.1	-	Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.13 (KHTML, like Gecko) Chrome/24.0.1290.1 Safari/537.13
			 * 判断依据:http://www.useragentstring.com/pages/Chrome/
			 */
			String temp = userAgent.substring(userAgent.indexOf("Chrome/") + 7);//拿到User Agent String "Chrome/" 之后的字符串,结果形如"24.0.1295.0 Safari/537.15"或"24.0.1295.0"
			String chromeVersion = null;
			if (temp.indexOf(" ") < 0) {//temp形如"24.0.1295.0"
				chromeVersion = temp;
			} else {//temp形如"24.0.1295.0 Safari/537.15"
				chromeVersion = temp.substring(0, temp.indexOf(" "));
			}
			return "Chrome"+chromeVersion+platformType+platformSeries;
		} else if (userAgent.contains("Firefox")) {
			/**
			 * *******
			 * FF 系列
			 * *******
			 * Firefox 16.0.1	-	Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1
			 * Firefox 15.0a2	-	Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20120716 Firefox/15.0a2
			 * Firefox 15.0.2	-	Mozilla/5.0 (Windows NT 6.2; WOW64; rv:15.0) Gecko/20120910144328 Firefox/15.0.2
			 * 判断依据:http://www.useragentstring.com/pages/Firefox/
			 */
			
			String temp = userAgent.substring(userAgent.indexOf("Firefox/") + 8);//拿到User Agent String "Firefox/" 之后的字符串,结果形如"16.0.1 Gecko/20121011"或"16.0.1"
			String ffVersion = null;
			if (temp.indexOf(" ") < 0) {//temp形如"16.0.1"
				ffVersion = temp;
			} else {//temp形如"16.0.1 Gecko/20121011"
				ffVersion = temp.substring(0, temp.indexOf(" "));
			}
			return "Firefox"+ffVersion+platformType+platformSeries;
		} else if (userAgent.contains("MSIE")||userAgent.indexOf("rv:11") > -1) {
			/**
			 * return judgeBrowser(userAgent, "Windows", "8");//判断浏览器
			 * *******
			 * IE 系列
			 * *******
			 * MSIE 10.0	-	Internet Explorer 10
			 * MSIE 9.0	-	Internet Explorer 9
			 * MSIE 8.0	-	Internet Explorer 8 or IE8 Compatibility View/Browser Mode
			 * MSIE 7.0	-	Windows Internet Explorer 7 or IE7 Compatibility View/Browser Mode
			 * MSIE 6.0	-	Microsoft Internet Explorer 6
			 * 判断依据:http://msdn.microsoft.com/en-us/library/ms537503(v=vs.85).aspx
			 */
			if (userAgent.contains("MSIE 10.0")) {//Internet Explorer 10
				return "Internet Explorer"+"10"+platformType+platformSeries;
			} else if (userAgent.contains("MSIE 9.0")) {//Internet Explorer 9
				return "Internet Explorer"+"9"+platformType+platformSeries;
			} else if (userAgent.contains("MSIE 8.0")) {//Internet Explorer 8
				return "Internet Explorer"+"8"+platformType+platformSeries;
			} else if (userAgent.contains("MSIE 7.0")) {//Internet Explorer 7
				return "Internet Explorer"+"7"+platformType+platformSeries;
			} else if (userAgent.contains("MSIE 6.0")) {//Internet Explorer 6
				return "Internet Explorer"+"6"+platformType+platformSeries;
			} else if (userAgent.indexOf("rv:11") > -1) {//Internet Explorer 6
				return "Internet Explorer"+"11"+platformType+platformSeries;
			}
		}else if(userAgent.contains("Android")){
			userAgent=userAgent.substring(userAgent.indexOf("Android"));
			userAgent=userAgent.substring(0,userAgent.indexOf(";"));
			return userAgent;
			//Mozilla/5.0 (Linux; U; Android 4.1.2; zh-cn; GT-N7102 Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
		} else {//暂时支持以上三个主流.其它浏览器,待续...
			return userAgent;
		}
		return userAgent;
	}
	
	/**
	 * 根据useragent判断是否是手机访问
	 * @param userAgent
	 * @return
	 */
	public static boolean isWeiXin(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null)
			userAgent = "";
		if (userAgent.indexOf("micromessenger") > 0) {// 是微信浏览器
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 获取当前用户的session
	 * 
	 * @param req
	 * @return
	 */
	public static Users getLoginUser(HttpServletRequest request) {
		Users userobj=(Users)WebUtils.getSessionAttribute(request,WEBCONSTANTS.SESSION_USER);
		if(userobj!=null){
			return userobj;
		}else{
			return null;
		}
	}
	 /**
     * 获取开始查询时间
     * @param request
     * @param paramName 默认 startDate
     * @param defaultvalue  格式 yyyy-MM-dd
     * @return
     */
    public static Date getStartDate(HttpServletRequest request, String paramName,String defaultvalue){
    	Date result=null;
		String str = request.getParameter(paramName);
		if (str == null || str.equals("")) {
			if(defaultvalue==null||defaultvalue.equals("")){
				defaultvalue=DateUtil.getFirstDayOfMonth();
			}
			result= DateUtil.parseDate(defaultvalue);
			result.setHours(0);
			result.setMinutes(0);
			result.setSeconds(0);
		} else if (str.length() == 10) {
			result=DateUtil.parseDate(str);
			result.setHours(0);
			result.setMinutes(0);
			result.setSeconds(0);
		} else if(str.length() == 19){
			result=DateUtil.parseFullDateTime(str);
		}
		return result;
    }
    /**
     * 获取一天的结束查询时间，2014-10-11：23：59：59
     * @param request
     * @param paramName 默认 endDate
     * @param defaultvalue 格式 yyyy-MM-dd
     * @return
     */
    public static Date getEndDate(HttpServletRequest request, String paramName,String defaultvalue){
    	Date result=null;
		String str = request.getParameter(paramName);
		if (str == null || str.equals("")) {
			if(defaultvalue==null||defaultvalue.equals("")){
				defaultvalue=DateUtil.getCurDate();
			}
			result= DateUtil.parseDate(defaultvalue);
			result.setHours(23);
			result.setMinutes(59);
			result.setSeconds(59);
		} else if (str.length() == 10) {
			result=DateUtil.parseDate(str);
			result.setHours(23);
			result.setMinutes(59);
			result.setSeconds(59);
		} else if(str.length() == 19){
			result=DateUtil.parseFullDateTime(str);
		}
		return result;
    }
  //判断当前请求是否为Ajax
  	public static boolean isAjaxRequest(HttpServletRequest request) {
  		String header = request.getHeader("X-Requested-With");
  		return !StringUtil.isNullOrEmpty(header) && "XMLHttpRequest".equals(header);
  	}
  	/**
	 * 重定向
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public static void redirectUrl(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,String url){
		try {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重定向到http://的url
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public static void redirectHttpUrl(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,String url){
		try {
			httpServletResponse.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getRequestFullUriNoContextPath(HttpServletRequest request){
		String port = "";
		if(request.getServerPort() != 80){
			port = ":" + request.getServerPort();
		}
		return request.getScheme() + "://" + request.getServerName() + port + request.getServletPath();
	}
	/**
     * 获取客户端IP地址.<br>
     * 支持多级反向代理
     * 
     * @param request
     *            HttpServletRequest
     * @return 客户端真实IP地址
     */
    public static String getRemoteAddr(final HttpServletRequest request) {
        try{
            String remoteAddr = request.getHeader("X-Forwarded-For");
            // 如果通过多级反向代理，X-Forwarded-For的值不止一个，而是一串用逗号分隔的IP值，此时取X-Forwarded-For中第一个非unknown的有效IP字符串
            if (isEffective(remoteAddr) && (remoteAddr.indexOf(",") > -1)) {
                String[] array = remoteAddr.split(",");
                for (String element : array) {
                    if (isEffective(element)) {
                        remoteAddr = element;
                        break;
                    }
                }
            }
            if (!isEffective(remoteAddr)) {
                remoteAddr = request.getHeader("X-Real-IP");
            }
            if (!isEffective(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            return remoteAddr;
        }catch(Exception e){
            return "";
        }
    }
    
    /**
     * 远程地址是否有效.
     * 
     * @param remoteAddr
     *            远程地址
     * @return true代表远程地址有效，false代表远程地址无效
     */
    private static boolean isEffective(final String remoteAddr) {
        boolean isEffective = false;
        if ((null != remoteAddr) && (!"".equals(remoteAddr.trim()))
                && (!"unknown".equalsIgnoreCase(remoteAddr.trim()))) {
            isEffective = true;
        }
        return isEffective;
    }
}
