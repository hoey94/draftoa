package com.starsoft.core.util;

import java.sql.Timestamp;
import java.util.UUID;

public class EncryptUtil {
	private Timestamp outDate;
	private String secretKey;
	public EncryptUtil() {
		 secretKey = UUID.randomUUID().toString(); // 密钥
	     outDate = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000);// 30分钟后过期
	}
	public EncryptUtil(String secretKey,Timestamp outDate) {
		if(secretKey==null || outDate==null){
			this.secretKey = UUID.randomUUID().toString(); // 密钥
			this.outDate = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000);// 30分钟后过期
		}else{
			 this.secretKey = secretKey; // 密钥
		     this.outDate = outDate;// 30分钟后过期
		}
	}
	/**
	 * 邮件找回密码数字签名工具
	 * @param userID 用户ID
	 * @return List ：
	 *    【0】Timestamp outDate
	 */
	public String getdigitalSignature(String userID){
	      long date = outDate.getTime() / 1000 * 1000;// 忽略毫秒数  mySql 取出时间是忽略毫秒数的
	      String key ="admin" + "@" + date + "@" + secretKey;
	      String digitalSignature = HashUtil.toMD5(key);
	      return digitalSignature.toUpperCase();
	}
	/**
	 * 是否超时如果超时，没有超时返回true；
	 * @param outDate
	 * @return
	 */
	public static boolean getcurrentDate(Timestamp outDate){
		Timestamp current=new Timestamp(System.currentTimeMillis());
		if(outDate!=null){
			if(current.before(outDate)){
				return true;
			}
		}
		return false;
	}
	
	public Timestamp getOutDate() {
		return outDate;
	}
	public String getSecretKey() {
		return secretKey;
	}
}
