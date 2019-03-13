package com.flowable.oa.core.service.impl;

import com.flowable.oa.core.entity.BizFile;
import com.flowable.oa.core.service.IBizFileService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午3:09:51
 * @author : lukewei
 * @description :
 */
@Service
public class BizFileServiceImplImpl extends BaseServiceImpl<BizFile> implements IBizFileService {

    @Override
    public List<BizFile> loadBizFilesByBizId(Integer bizId, String taskId) {

        BizFile bizFile = new BizFile();
        bizFile.setBizId(bizId);
        bizFile.setTaskId(taskId);
        return this.findByModel(bizFile, false);
    }

}
