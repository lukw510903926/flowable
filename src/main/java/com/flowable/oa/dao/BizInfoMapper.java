package com.flowable.oa.dao;

import java.util.List;
import java.util.Map;
import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.util.mybatis.MyMapper;

public interface BizInfoMapper extends MyMapper<BizInfo> {

	List<BizInfo> queryWorkOrder(Map<String, Object> params);

}
