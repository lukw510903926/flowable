package com.flowable.oa.service;

import java.util.List;
import java.util.Map;
import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.util.mybatis.IBaseService;
import com.github.pagehelper.PageInfo;

/**
 * 工单信息业务<br>
 */
public interface IBizInfoService extends IBaseService<BizInfo> {

    void saveOrUpdate(BizInfo bizInfo);

    /**
     * 复制工单
     *
     * @param bizId
     * @param processInstanceId
     * @param variables
     * @return
     */
    BizInfo copyBizInfo(String bizId, String processInstanceId, Map<String, Object> variables);

    /**
     * 分页查询指定用户创建的工单
     *
     * @return
     */
    PageInfo<BizInfo> findBizInfo(Map<String, Object> params, PageInfo<BizInfo> page);

    List<BizInfo> getBizByParentId(String parentId);
}
