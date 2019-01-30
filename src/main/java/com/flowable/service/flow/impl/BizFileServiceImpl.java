package com.flowable.service.flow.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowable.dao.flow.IBizFileDao;
import com.flowable.entity.BizFile;
import com.flowable.service.flow.IBizFileService;

/**
 * @creater lukew
 */
@Service
public class BizFileServiceImpl implements IBizFileService {

    @Autowired
    private IBizFileDao dao;

    @Override
    @Transactional
    public void addBizFile(BizFile... beans) {
        if (beans == null) {
            return;
        }
        for (BizFile bean : beans) {
            dao.save(bean);
        }
    }

    @Override
    @Transactional
    public void updateBizFile(BizFile... beans) {
        if (beans == null) {
            return;
        }
        for (BizFile bean : beans) {
            if (bean.getId() == null) {
                continue;
            }
            dao.update(bean);
        }
    }

    @Override
    @Transactional
    public void deleteBizFile(BizFile... beans) {
        if (beans == null) {
            return;
        }
        for (BizFile bean : beans) {
            if (bean.getId() == null) {
                continue;
            }
            dao.delete(bean);
        }
    }

    @Override
    @Transactional
    public void deleteBizFile(String... ids) {
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            dao.deleteById(id);
        }
    }

    @Override
    public List<BizFile> loadBizFilesByBizId(String bizId, String taskId) {
        BizFile bizFile = new BizFile();
        bizFile.setBizId(bizId);
        bizFile.setTaskId(taskId);
        return dao.findBizFile(bizFile);
    }

    @Override
    public BizFile getBizFileById(String id) {

        return dao.getById(id);
    }

    @Override
    public List<BizFile> findBizFile(BizFile bizFile) {

        return this.dao.findBizFile(bizFile);
    }

}
