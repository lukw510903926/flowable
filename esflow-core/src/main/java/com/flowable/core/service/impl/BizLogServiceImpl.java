package com.flowable.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.core.bean.BizLog;
import com.flowable.core.dao.IBizLogDao;
import com.flowable.core.service.IBizLogService;

@Service
@Transactional(readOnly = true)
public class BizLogServiceImpl implements IBizLogService {

	@Autowired
	private IBizLogDao logDao;

	@Transactional
	public void addBizLog(BizLog... beans)  {
		for (BizLog bean : beans){
			logDao.save(bean);
		}
	}

	@Override
	public List<BizLog> loadBizLogs(String workID)  {
		return logDao.loadLogByBizId(workID);
	}

	public BizLog getBizLogById(String logId){
		return logDao.getById(logId);
	}
}
