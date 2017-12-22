package com.flowable.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @author 26223
 * @time 2016年10月10日
 * @email lukw@eastcom-sw.com
 */
@SuppressWarnings("rawtypes")
public class DataGrid implements Serializable{

	private static final long serialVersionUID = 1L;

	private List  rows ;
	
	private long total;

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
