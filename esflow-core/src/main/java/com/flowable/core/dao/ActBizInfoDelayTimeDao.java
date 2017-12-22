package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.ActBizInfoDelayTime;

public interface ActBizInfoDelayTimeDao extends IBaseDao<ActBizInfoDelayTime>{

	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId,String taskName);

	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime);

}
