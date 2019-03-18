package com.flowable.oa.core.dao;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.util.mybatis.MyMapper;
import com.flowable.oa.core.vo.BizInfoVo;
import java.util.List;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/18 17:16
 **/
public interface BizInfoMapper extends MyMapper<BizInfo> {

	List<BizInfo> queryWorkOrder(BizInfoVo bizInfoVo);
}
