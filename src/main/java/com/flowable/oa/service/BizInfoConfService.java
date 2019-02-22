package com.flowable.oa.service;

import java.util.List;

import com.flowable.oa.entity.BizInfoConf;
import com.flowable.oa.util.mybatis.IBaseService;

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
