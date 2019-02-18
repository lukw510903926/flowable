package com.flowable.oa.service;

import java.util.List;

import com.flowable.oa.entity.BizLog;
import com.flowable.oa.util.mybatis.IBaseService;

public interface IBizLogService extends IBaseService<BizLog> {

	void addBizLog(BizLog... beans) ;

	List<BizLog> loadBizLogs(String bizId) ;

}
