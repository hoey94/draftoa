package com.starsoft.core.util;


/**
 * 调用返回通用数据结果模版
 * @date 2012-04-13
 */
public class HttpResponseResult{
	public static final String SUCCESS="1";
	public static final String FAIL="0";
	private String resultCode;
	private String resultDesc;
	private String data;
	public HttpResponseResult(){
		super();
		this.setResultCode(FAIL);
	}
	/***
	 * 调用返回通用数据结果模版
	 * @param resultCode 返回的结果标识
	 * @param resultDesc 返回的结果描述
	 * @param data 返回的数据对象
	 */
	public HttpResponseResult(String resultCode, String resultDesc,String data) {
		super();
		this.resultCode = resultCode;
		this.resultDesc = resultDesc;
		this.data = data;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
