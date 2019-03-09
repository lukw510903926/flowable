package com.flowable.oa.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowable.oa.core.entity.ProcessVariable;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import com.flowable.oa.core.service.IProcessVariableService;
import com.flowable.oa.core.service.IVariableInstanceService;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessVariableServiceImplImpl extends BaseServiceImpl<ProcessVariable> implements IProcessVariableService {

    @Autowired
    private IVariableInstanceService variableInstanceService;

    @Override
    @Transactional
    public void deleteVariable(List<String> list) {

        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().filter(StringUtils::isNotBlank).forEach(variableId -> {
                ProcessVariableInstance instance = new ProcessVariableInstance();
                instance.setVariableId(variableId);
                this.variableInstanceService.deleteByModel(instance);
                this.deleteById(variableId);
            });
        }
    }

    @Override
    public PageInfo<ProcessVariable> findProcessVariables(ProcessVariable variable, PageInfo<ProcessVariable> page) {

        PageUtil.startPage(page);
        return new PageInfo<>(this.select(variable));
    }

    @Override
    @Transactional
    public void copyVariables(ProcessDefinition oldPdf, ProcessDefinition newPdf) {

        if (oldPdf != null && newPdf != null) {
            Map<String, String> refMap = new HashMap<>();
            // 拷贝全局配置
            ProcessVariable example = new ProcessVariable();
            example.setProcessDefinitionId(oldPdf.getId());
            List<ProcessVariable> processValBeans = this.findByModel(example, false);
            List<ProcessVariable> processRefList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(processValBeans)) {
                for (ProcessVariable valBean : processValBeans) {
                    ProcessVariable processVar = valBean.clone();
                    processVar.setProcessDefinitionId(newPdf.getId());
                    save(processVar);
                    refMap.put(valBean.getId(), processVar.getId());
                    if (StringUtils.isNotBlank(processVar.getRefVariable())) {
                        processRefList.add(processVar);
                    }
                }
            }
            for (ProcessVariable tv : processRefList) {
                tv.setRefVariable(refMap.get(tv.getRefVariable()));
                this.updateNotNull(tv);
            }
        }
    }
}
