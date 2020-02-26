package com.flowable.oa.core.service.impl;

import com.flowable.oa.core.entity.ProcessVariable;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import com.flowable.oa.core.service.IProcessVariableService;
import com.flowable.oa.core.service.IVariableInstanceService;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020/2/20 5:55 下午
 */
@Service
public class ProcessVariableServiceImplImpl extends BaseServiceImpl<ProcessVariable> implements IProcessVariableService {

    @Autowired
    private IVariableInstanceService variableInstanceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVariable(List<Long> list) {

        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().filter(Objects::nonNull).forEach(variableId -> {
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
    @Transactional(rollbackFor = Exception.class)
    public void copyVariables(ProcessDefinition oldPdf, ProcessDefinition newPdf) {

        if (oldPdf != null && newPdf != null) {
            Map<Long, Long> refMap = new HashMap<>();
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
                    if (null != processVar.getRefVariable()) {
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
