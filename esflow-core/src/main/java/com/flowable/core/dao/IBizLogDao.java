package com.flowable.core.dao;

import java.util.List;
import java.util.Map;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.BizLog;

public interface IBizLogDao extends IBaseDao<BizLog> {

	public List<BizLog> loadLogByBizId(String bizId) ;

	public List<Map<String,Object>> findBizInfoIds(String handleUser);

	/**
	 * 获取指定处理结果的 日志
	 * @return
	 */
	public List<BizLog> findBizLogs(BizLog bizLog);
}
