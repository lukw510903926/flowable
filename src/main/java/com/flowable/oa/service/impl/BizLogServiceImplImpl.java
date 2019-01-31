package com.flowable.oa.service.impl;

import java.util.List;

import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flowable.oa.entity.BizLog;
import com.flowable.oa.service.IBizLogService;

@Service
public class BizLogServiceImplImpl extends BaseServiceImpl<BizLog> implements IBizLogService {

	@Override
	@Transactional
	public void addBizLog(BizLog... beans)  {
		for (BizLog bean : beans){
			this.save(bean);
		}
	}

	@Override
	public List<BizLog> loadBizLogs(String workID)  {
		
		BizLog bizlog = new BizLog();
		bizlog.setBizId(workID);
		return this.findByModel(bizlog, false);
	}

	@Override
	public BizLog getBizLogById(String logId){
		return this.selectByKey(logId);
	}
}
