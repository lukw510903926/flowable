package com.flowable.oa.core.service.impl;

import java.util.List;

import com.flowable.oa.core.entity.BizLog;
import com.flowable.oa.core.service.IBizLogService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<BizLog> loadBizLogs(String bizId)  {
		
		BizLog bizlog = new BizLog();
		bizlog.setBizId(bizId);
		return this.select(bizlog);
	}
}
