package com.flowable.oa.core.service.impl;

import java.util.*;

import com.flowable.oa.core.entity.BizFile;
import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizInfoConf;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import com.flowable.oa.core.entity.auth.SystemUser;
import com.flowable.oa.core.service.BizInfoConfService;
import com.flowable.oa.core.service.IBizFileService;
import com.flowable.oa.core.service.IBizInfoService;
import com.flowable.oa.core.service.IVariableInstanceService;
import com.flowable.oa.core.service.auth.ISystemUserService;
import com.flowable.oa.core.util.Constants;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.WebUtil;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import com.flowable.oa.core.vo.BaseVo;
import com.flowable.oa.core.vo.BizInfoVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BizInfoServiceImplImpl extends BaseServiceImpl<BizInfo> implements IBizInfoService {

    private Logger logger = LoggerFactory.getLogger(BizInfoServiceImplImpl.class);

    @Autowired
    private BizInfoConfService bizInfoConfService;

    @Autowired
    private IVariableInstanceService variableInstanceService;

    @Autowired
    private IBizFileService bizFileService;

    @Autowired
    private ISystemUserService userService;

    @Override
    public List<BizInfo> getBizByParentId(Integer parentId) {

        BizInfo bizInfo = new BizInfo();
        bizInfo.setParentId(parentId);
        return this.select(bizInfo);
    }

    @Override
    @Transactional
    public void saveOrUpdate(BizInfo bizInfo) {

        if (bizInfo.getId() != null) {
            this.updateNotNull(bizInfo);
        } else {
            this.save(bizInfo);
        }
    }

    @Override
    public BizInfo copyBizInfo(Integer bizId, String processInstanceId, Map<String, Object> variables) {

        BizInfo oldBiz = this.selectByKey(bizId);
        List<BizInfo> list = this.getBizByParentId(bizId);
        BizInfo newBiz = oldBiz.clone();
        newBiz.setId(null);
        if (CollectionUtils.isEmpty(list)) {
            newBiz.setWorkNum(newBiz.getWorkNum() + "-00" + 1);
        } else {
            newBiz.setWorkNum(newBiz.getWorkNum() + "-00" + (list.size() + 1));
        }
        newBiz.setProcessInstanceId(processInstanceId);
        newBiz.setParentId(bizId);
        newBiz.setCreateTime(new Date());
        newBiz.setParentTaskName(oldBiz.getTaskName());
        newBiz.setStatus(Constants.BIZ_NEW);
        String username = WebUtil.getLoginUsername();
        newBiz.setCreateUser(username);
        this.saveOrUpdate(newBiz);
        this.copyProcessVarInstance(processInstanceId, oldBiz, newBiz);
        this.copyBizInfoConf(newBiz, oldBiz.getId());
        this.copyBizFile(bizId, oldBiz, newBiz, username);
        return newBiz;
    }

    private void copyProcessVarInstance(String processInstanceId, BizInfo oldBiz, BizInfo newBiz) {

        ProcessVariableInstance instance = new ProcessVariableInstance();
        instance.setBizId(oldBiz.getId());
        instance.setTaskId(Constants.TASK_START);
        List<ProcessVariableInstance> processInstances = variableInstanceService.select(instance);
        if (CollectionUtils.isNotEmpty(processInstances)) {
            processInstances.forEach(oldInstance -> {
                ProcessVariableInstance processVariableInstance = oldInstance.clone();
                processVariableInstance.setId(null);
                processVariableInstance.setBizId(newBiz.getId());
                processVariableInstance.setProcessInstanceId(processInstanceId);
                processVariableInstance.setCreateTime(new Date());
                variableInstanceService.save(processVariableInstance);
            });
        }
    }

    private void copyBizFile(Integer bizId, BizInfo oldBiz, BizInfo newBiz, String username) {

        List<BizFile> files = bizFileService.loadBizFilesByBizId(bizId, oldBiz.getTaskId());
        if (CollectionUtils.isNotEmpty(files)) {
            files.forEach(oldFile -> {
                BizFile bizFile = oldFile.clone();
                bizFile.setId(null);
                bizFile.setBizId(newBiz.getId());
                bizFile.setCreateDate(new Date());
                bizFile.setCreateUser(username);
                bizFileService.addBizFile(bizFile);
            });
        }
    }

    private void copyBizInfoConf(BizInfo newBiz, Integer bizId) {

        BizInfoConf example = new BizInfoConf();
        example.setBizId(bizId);
        List<BizInfoConf> list = this.bizInfoConfService.select(example);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(bizConf -> {
                BizInfoConf newConf = bizConf.clone();
                newConf.setId(null);
                newConf.setBizId(newBiz.getId());
                newConf.setCreateTime(new Date());
                bizInfoConfService.save(newConf);
            });
        }
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> list) {

        list.forEach(id -> {
            if (id != null) {
                BizInfo bizInfo = new BizInfo();
                bizInfo.setId(id);
                bizInfo.setStatus(Constants.BIZ_DELETE);
                this.updateNotNull(bizInfo);
            }
        });
    }

    @Override
    public PageInfo<BizInfo> findBizInfo(BizInfoVo bizInfoVo, PageInfo<BaseVo> page) {

        logger.info("工单查询 bizInfoVo : " + bizInfoVo);
        PageUtil.startPage(page);
        BizInfo bizInfo = new BizInfo();
        BeanUtils.copyProperties(bizInfoVo, bizInfo);
        List<BizInfo> list = this.select(bizInfo);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(entity -> {
                entity.setCreateUser(Optional.ofNullable(this.userService.getUserByUsername(entity.getCreateUser())).map(SystemUser::getName).orElse(entity.getCreateUser()));
                if (StringUtils.isNotBlank(entity.getTaskAssignee())) {
                    entity.setTaskAssignee(Optional.ofNullable(this.userService.getUserByUsername(entity.getTaskAssignee())).map(SystemUser::getName).orElse(entity.getTaskAssignee()));
                }
            });
        }
        return new PageInfo<>(list);
    }
}
