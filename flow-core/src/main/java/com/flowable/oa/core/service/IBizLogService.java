package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizLog;
import com.flowable.oa.core.util.mybatis.IBaseService;

import java.util.List;


public interface IBizLogService extends IBaseService<BizLog> {

	void addBizLog(BizLog... beans) ;

	List<BizLog> loadBizLogs(Integer bizId) ;

}
