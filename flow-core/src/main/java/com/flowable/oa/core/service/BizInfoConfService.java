package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.util.mybatis.IBaseService;

import java.util.List;


/**
 * @author 2622
 * @time 2016年5月30日
 * @email lukw@eastcom-sw.com
 */
public interface BizInfoConfService extends IBaseService<BizInfoConf> {

    /**
     * 当前工单中我的待办
     *
     * @param bizId
     * @return
     */
    BizInfoConf getMyWork(String bizId);

    void saveOrUpdate(BizInfoConf bizInfoConf);

	List<BizInfoConf> findByBizId(String bizId);

	void deleteByBizId(String bizId);

}
