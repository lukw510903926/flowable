package com.flowable.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.flowable.common.utils.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.dao.IProcessModelDao;
import com.flowable.core.dao.IProcessVariableDao;

@Repository
public class ProcessModelDaoImpl implements IProcessModelDao {

    @Autowired
    private IProcessVariableDao processVariableDao;

    @Override
    public void copyVariables(ProcessDefinition oldPdf, ProcessDefinition newPdf) throws Exception {

        if (oldPdf != null && newPdf != null) {

            int version_ = newPdf.getVersion();
            Map<String, String> refmap = new HashMap<String, String>();

            // 拷贝全局配置
            List<ProcessVariable> processValBeans = processVariableDao.loadProcessVariables(oldPdf.getId(), oldPdf.getVersion());
            List<ProcessVariable> processRefList = new ArrayList<ProcessVariable>();
            if (CollectionUtils.isNotEmpty(processValBeans)) {
                for (ProcessVariable valBean : processValBeans) {
                    ProcessVariable processVar = valBean.clone();
                    processVar.setId(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
                    processVar.setProcessDefinitionId(newPdf.getId());
                    processVar.setVersion(version_);
                    String id = (String) processVariableDao.save(processVar);
                    refmap.put(valBean.getId(), id);
                    if (StringUtils.isNotBlank(processVar.getRefVariable())) {
                        processRefList.add(processVar);
                    }
                }
            }
            for (ProcessVariable tv : processRefList) {
                tv.setRefVariable(refmap.get(tv.getRefVariable()));
                processVariableDao.update(tv);
            }
            // 拷贝任务配置
            List<ProcessVariable> newRefTaskVars = new ArrayList<ProcessVariable>();
            ProcessVariable variable = new ProcessVariable();
            variable.setProcessDefinitionId(oldPdf.getId());
            variable.setVersion(oldPdf.getVersion());
            List<ProcessVariable> taskValBeans = processVariableDao.findProcessVariables(variable,new PageHelper<>()).getList();
            if (CollectionUtils.isNotEmpty(taskValBeans)) {
                for (ProcessVariable oldValBean : taskValBeans) {
                    ProcessVariable taskVar = oldValBean.clone();
                    taskVar.setId(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
                    taskVar.setProcessDefinitionId(newPdf.getId());
                    taskVar.setVersion(version_);
                    String id = (String) processVariableDao.save(taskVar);
                    refmap.put(oldValBean.getId(), id);
                    if (StringUtils.isNotBlank(taskVar.getRefVariable())) {
                        newRefTaskVars.add(taskVar);
                    }
                }
            }

            for (ProcessVariable tv : newRefTaskVars) {
                tv.setRefVariable(refmap.get(tv.getRefVariable()));
                processVariableDao.update(tv);
            }
        }
    }
}
