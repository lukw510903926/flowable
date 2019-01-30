package com.flowable.service.flow.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.dao.flow.IBizLogDao;
import com.flowable.entity.BizLog;
import com.flowable.service.flow.IBizLogService;

@Service
@Transactional(readOnly = true)
public class BizLogServiceImpl implements IBizLogService {

	@Autowired
	private IBizLogDao logDao;

	@Override
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

	@Override
	public BizLog getBizLogById(String logId){
		return logDao.getById(logId);
	}
}
