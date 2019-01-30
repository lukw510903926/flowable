package com.flowable.dao.flow;

import java.util.List;

import com.flowable.entity.ActBizInfoDelayTime;
import com.flowable.util.dao.IBaseDao;

public interface ActBizInfoDelayTimeDao extends IBaseDao<ActBizInfoDelayTime>{

	ActBizInfoDelayTime findActBizInfoByBizId(String bizId,String taskName);

	List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime);

}
