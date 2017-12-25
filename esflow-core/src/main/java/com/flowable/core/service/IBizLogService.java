package com.flowable.core.service;

import java.util.List;

import com.flowable.core.bean.BizLog;

public interface IBizLogService {

	void addBizLog(BizLog... beans) ;

	List<BizLog> loadBizLogs(String bizId) ;

	BizLog getBizLogById(String id) ;
}
