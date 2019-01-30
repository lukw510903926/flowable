package com.flowable.service.flow;

import java.util.List;
import java.util.Map;

import com.flowable.entity.BizInfo;
import com.flowable.util.PageHelper;
import com.flowable.util.service.IBaseService;

/**
 * 工单信息业务<br>
 */
public interface IBizInfoService extends IBaseService<BizInfo> {

    /**
     * 添加工单信息
     *
     * @param beans
     */
    void addBizInfo(BizInfo... beans);

    /**
     * 更新工单系信息
     *
     * @param beans
     */
    void updateBizInfo(BizInfo... beans);

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
     * 删除工单信息
     *
     * @param beans
     */
    void deleteBizInfo(BizInfo... beans);

    /**
     * 删除工单信息
     *
     * @param ids
     */
    void deleteBizInfo(String... ids);

    /**
     * 包含配置信息 taskId
     *
     * @param id
     * @return
     */
    BizInfo getByBizId(String id);

    /**
     * 分页查询指定用户创建的工单
     *
     * @return
     */
    PageHelper<BizInfo> findBizInfo(Map<String, Object> params, PageHelper<BizInfo> page);

    /**
     * @param list
     */
    void updateBizByIds(List<String> list);

    List<String> loadBizInfoStatus(String processId);

    List<BizInfo> getBizByParentId(String parentId);
}
