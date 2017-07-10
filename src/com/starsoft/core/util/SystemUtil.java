package com.starsoft.core.util;

/*
 * Created on 2005-6-5
 * Author stephen
 * CopyRight(C)2005-2008 , All rights reserved.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * 与系统相关的一些常用工具方法.
 * 
 * @author stephen
 * @version 1.0.0
 */
public class SystemUtil {

	/**
	 * 获取当前操作系统名称.
	 * return 操作系统名称 例如:windows xp,linux 等.
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}
	public static String getOSLanguage() {
		return System.getProperty("os.name").toLowerCase();
	}
	public static String getOSArch() {
		return System.getProperty("os.arch").toLowerCase();
	}
	public static String getOSVersion() {
		return System.getProperty("os.version").toLowerCase();
	}
	public static String getOSCharset() {
		return System.getProperty("file.encoding").toLowerCase();
	}
	public static String getOSLanguages() {
		return Locale.getDefault().getDisplayLanguage();
	}

	/**
	 * 获取unix网卡的mac地址.
	 * 非windows的系统默认调用本方法获取.如果有特殊系统请继续扩充新的取mac地址方法.
	 * @return mac地址
	 */
	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ifconfig eth0");// linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("hwaddr");// 寻找标示字符串[hwaddr]
				if (index >= 0) {// 找到了
					mac = line.substring(index +"hwaddr".length()+ 1).trim();//  取出mac地址并去除2边空格
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * 获取widnows网卡的mac地址.
	 * @return mac地址
	 */
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			String languages=getOSLanguages();
			process = Runtime.getRuntime().exec("ipconfig /all");// windows下的命令，显示信息中包含有mac地址信息
			if("中文".equals(languages)){
				bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GB2312"));
				String line = null;
				int index = -1;
				while ((line = bufferedReader.readLine()) != null) {
					index = line.toLowerCase().indexOf("物理地址");// 寻找标示字符串[physical address]
					if (index >= 0) {// 找到了
						index = line.indexOf(":");// 寻找":"的位置
						if (index>=0) {
							mac = line.substring(index + 1).trim();//  取出mac地址并去除2边空格
						}
						break;
					}
				}
			}else{
				bufferedReader = new BufferedReader(new InputStreamReader(process
						.getInputStream()));
				String line = null;
				int index = -1;
				while ((line = bufferedReader.readLine()) != null) {
					index = line.toLowerCase().indexOf("physical address");// 寻找标示字符串[physical address]
					if (index >= 0) {// 找到了
						index = line.indexOf(":");// 寻找":"的位置
						if (index>=0) {
							mac = line.substring(index + 1).trim();//  取出mac地址并去除2边空格
						}
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * 测试用的main方法.
	 * 
	 * @param argc
	 *            运行参数.
	 */
	public static void main(String[] argc) {
		String os = getOSName();
		if(os.startsWith("windows")){
			//本地是windows
			String mac = getWindowsMACAddress();
			System.out.println(mac);
		}else{
			//本地是非windows系统 一般就是unix
			String mac = getUnixMACAddress();
			System.out.println(mac);
		}
	}
}


