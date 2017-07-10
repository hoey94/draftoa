package com.starsoft.core.exception;

public class OperateException extends RuntimeException {

	/**
	 * 数据底层操作异常
	 */
	private static final long serialVersionUID = 1L;
	
	public OperateException() {
		super();
	}

	public OperateException(String message) {
		super(message);
	}

}
