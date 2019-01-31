package com.flowable.oa.service.impl;

import java.util.List;

import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flowable.oa.entity.ActBizInfoDelayTime;
import com.flowable.oa.service.ActBizInfoDelayTimeService;

@Service
public class ActBizInfoDelayTimeServiceImplImpl extends BaseServiceImpl<ActBizInfoDelayTime>
		implements ActBizInfoDelayTimeService {

	@Override
	@Transactional
	public void saveOrUpdate(ActBizInfoDelayTime actBizInfo) {

		if (StringUtils.isNotBlank(actBizInfo.getId())) {
			this.updateNotNull(actBizInfo);
		} else {
			this.save(actBizInfo);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId, String taskId) {

		if (StringUtils.isBlank(bizId)) {
			return null;
		}
		ActBizInfoDelayTime delayTime = new ActBizInfoDelayTime();
		delayTime.setTaskId(taskId);
		delayTime.setBizId(bizId);
		List<ActBizInfoDelayTime> list = this.findByModel(delayTime, false);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime) {

		return this.findByModel(delayTime, false);
	}
}
