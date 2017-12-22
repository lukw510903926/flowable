package com.flowable.core.service;

import java.util.List;

import com.flowable.core.bean.BizLog;

public interface IBizLogService {

	public void addBizLog(BizLog... beans) ;

	public List<BizLog> loadBizLogs(String bizId) ;

	public BizLog getBizLogById(String id) ;
}
