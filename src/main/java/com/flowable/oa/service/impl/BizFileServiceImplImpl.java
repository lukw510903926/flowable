package com.flowable.oa.service.impl;

import java.util.List;

import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flowable.oa.entity.BizFile;
import com.flowable.oa.service.IBizFileService;

/**
 * 
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午3:09:51
 * @author : lukewei
 * @description :
 */
@Service
public class BizFileServiceImplImpl extends BaseServiceImpl<BizFile> implements  IBizFileService {

    @Override
    @Transactional
    public void addBizFile(BizFile... beans) {
        if (beans == null) {
            return;
        }
        for (BizFile bean : beans) {
           this.save(bean);
        }
    }

    @Override
    public List<BizFile> loadBizFilesByBizId(String bizId, String taskId) {

        BizFile bizFile = new BizFile();
        bizFile.setBizId(bizId);
        bizFile.setTaskId(taskId);
        return this.findByModel(bizFile, false);
    }

    @Override
    public List<BizFile> findBizFile(BizFile bizFile) {

        return this.findByModel(bizFile, false);
    }

}
