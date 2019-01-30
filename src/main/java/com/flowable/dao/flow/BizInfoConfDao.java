package com.flowable.dao.flow;

import java.util.List;
import java.util.Map;

import com.flowable.entity.BizInfoConf;
import com.flowable.util.dao.IBaseDao;

/**
 * @author 2622
 * @time 2016年5月28日
 * @email lukw@eastcom-sw.com
 */
public interface BizInfoConfDao extends IBaseDao<BizInfoConf> {

    List<BizInfoConf> findBizInfoConf(BizInfoConf bizInfoConf);

    /**
     * 当前工单我的待办
     *
     * @param bizId
     * @return
     */
    BizInfoConf getMyWork(String bizId);

    List<BizInfoConf> getBizInfoConf(String bizId);

    /**
     * 工单转派 (不通过工作流)
     *
     * @param map
     */
    void turnTask(Map<String, Object> map);
}
