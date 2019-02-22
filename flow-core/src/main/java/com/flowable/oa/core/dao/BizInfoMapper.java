package com.flowable.oa.core.dao;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.util.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BizInfoMapper extends MyMapper<BizInfo> {

	List<BizInfo> queryWorkOrder(Map<String, Object> params);

}
