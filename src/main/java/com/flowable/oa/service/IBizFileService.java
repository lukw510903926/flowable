package com.flowable.oa.service;

import java.util.List;

import com.flowable.oa.util.mybatis.IBaseService;
import com.flowable.oa.entity.BizFile;

/**
 * 工单附件
 */
public interface IBizFileService extends IBaseService<BizFile> {

    /**
     * 添加工单附件
     *
     * @param beans
     * @
     */
    void addBizFile(BizFile... beans);

    /**
     * 更新工单系信息
     *
     * @param beans
     * @
     */
    void updateBizFile(BizFile... beans);

    /**
     * 删除工单信息
     *
     * @param beans
     * @
     */
    void deleteBizFile(BizFile... beans);

    /**
     * 删除工单信息
     *
     * @param ids
     * @
     */
    void deleteBizFile(String... ids);

    /**
     * 根据工单对象ID获取该工单的附件列表
     *
     * @param bizId
     * @param taskId
     * @return
     * @
     */
    List<BizFile> loadBizFilesByBizId(String bizId, String taskId);

    /**
     * 根据ID获取附件对象
     *
     * @param id
     * @return
     * @
     */
    BizFile getBizFileById(String id);

    List<BizFile> findBizFile(BizFile bizFile);

}
