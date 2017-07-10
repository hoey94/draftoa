package com.starsoft.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 信息摘要帮助类
 * @author 
 * @version v1.0
 */
public class MessageDigestUtil {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	/**
	 * 将字节数组转换为16进制的字符串
	 * @param byteArray 字节数组
	 * @return 16进制的字符串
	 */
	private static String byteArrayToHexString(byte[] byteArray){
		StringBuffer sb = new StringBuffer();
		for(byte byt:byteArray){
			sb.append(byteToHexString(byt));
		}
		return sb.toString();
	}
	/**
	 * 将字节转换为16进制字符串
	 * @param byt 字节
	 * @return 16进制字符串
	 */
	private static String byteToHexString(byte byt) {
		int n = byt;
		if (n < 0)
			n = 256 + n;
		return hexDigits[n/16] + hexDigits[n%16];
	}
	/**
	 * 将摘要信息转换为相应的编码
	 * @param code 编码类型
	 * @param message 摘要信息
	 * @return 相应的编码字符串
	 */
	private static String Encode(String code,String message){
		MessageDigest md;
		String encode = null;
		try {
			md = MessageDigest.getInstance(code);
			encode = byteArrayToHexString(md.digest(message
					.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encode;
	}
	/**
	 * 得到信息的MD5校验值
	 * @param message 摘要信息
	 * @return 
	 */
	public static String md5Encode(String message){
		return Encode("MD5",message);
	}
	/**
	 * 得到信息的SHA校验值
	 * @param message 摘要信息
	 * @return 
	 */
	public static String shaEncode(String message){
		return Encode("SHA",message);
	}
	/**
	 * 得到信息的SHA256校验值
	 * @param message 摘要信息
	 * @return SHA-256编码之后的字符串
	 */
	public static String sha256Encode(String message){
		return Encode("SHA-256",message);
	}
	/**
	 * 得到信息的SHA512校验值
	 * @param message 摘要信息
	 * @return SHA-512编码之后的字符串
	 */
	public static String sha512Encode(String message){
		return Encode("SHA-512",message);
	}
	
	/**
	 * 取到原始密码校验后的值
	 * @param password
	 * @return
	 */
	public static String encodePassword(String password){
		
		return Encode("SHA-512", password + WEBCONSTANTS.PASSWORD_SALT);
	}

	
	public static void main(String[] args) {
		System.out.println("--MD5--:"+MessageDigestUtil.md5Encode("test"));
		System.out.println("--SHA--:"+MessageDigestUtil.shaEncode("test"));
		System.out.println("SHA-256:"+MessageDigestUtil.sha256Encode("test"));
		System.out.println("SHA-512:"+MessageDigestUtil.sha512Encode("test"));
		System.out.println("SHA-512:"+MessageDigestUtil.encodePassword("1"));
	}
}
