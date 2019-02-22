package com.flowable.oa.core.dao;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.util.mybatis.MyMapper;
import com.flowable.oa.core.vo.BizInfoVo;
import java.util.List;

public interface BizInfoMapper extends MyMapper<BizInfo> {

	List<BizInfo> queryWorkOrder(BizInfoVo bizInfoVo);

}
