package com.flowable.service.flow;

import java.util.List;

import com.flowable.entity.BizLog;

public interface IBizLogService {

	void addBizLog(BizLog... beans) ;

	List<BizLog> loadBizLogs(String bizId) ;

	BizLog getBizLogById(String id) ;
}
