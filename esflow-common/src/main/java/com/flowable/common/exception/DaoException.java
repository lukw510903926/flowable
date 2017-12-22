package com.flowable.common.exception;

/**
 * 处理在操作数据访问对象时产生的异常
 * 
 */
public class DaoException extends Exception {
	private static final long serialVersionUID = 7076352211902647350L;

	public DaoException(String message) {
		super(message);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}
}
