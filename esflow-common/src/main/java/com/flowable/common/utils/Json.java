package com.flowable.common.utils;

import java.io.Serializable;

/**
 * @author 26223
 * @time 2016年10月10日
 * @email lukw@eastcom-sw.com
 */
public class Json implements Serializable{

	private static final long serialVersionUID = 1L;

	private boolean success = true;
	
	private String msg;
	
	private Object obj;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
