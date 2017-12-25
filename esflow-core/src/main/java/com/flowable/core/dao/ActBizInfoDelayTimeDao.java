package com.flowable.core.dao;

import java.util.List;

import com.flowable.common.dao.IBaseDao;
import com.flowable.core.bean.ActBizInfoDelayTime;

public interface ActBizInfoDelayTimeDao extends IBaseDao<ActBizInfoDelayTime>{

	ActBizInfoDelayTime findActBizInfoByBizId(String bizId,String taskName);

	List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime);

}
