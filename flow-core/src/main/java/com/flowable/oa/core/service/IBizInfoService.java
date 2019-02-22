package com.flowable.oa.core.service;

import java.util.List;
import java.util.Map;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.util.mybatis.IBaseService;
import com.flowable.oa.core.vo.BaseVo;
import com.flowable.oa.core.vo.BizInfoVo;
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
    PageInfo<BizInfo> findBizInfo(BizInfoVo bizInfoVo, PageInfo<BaseVo> page);

    List<BizInfo> getBizByParentId(String parentId);
}
