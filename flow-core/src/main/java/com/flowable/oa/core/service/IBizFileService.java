package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.BizFile;
import com.flowable.oa.core.util.mybatis.IBaseService;

import java.util.List;


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
     * 根据工单对象ID获取该工单的附件列表
     *
     * @param bizId
     * @param taskId
     * @return
     * @
     */
    List<BizFile> loadBizFilesByBizId(String bizId, String taskId);

    List<BizFile> findBizFile(BizFile bizFile);

}
