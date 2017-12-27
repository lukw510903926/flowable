package com.flowable.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.dao.IProcessVarInstanceDao;
import com.flowable.core.dao.IProcessVariableDao;
import com.flowable.core.service.IProcessVariableService;

@Service
@Transactional(readOnly = true)
public class ProcessVariableServiceImpl implements IProcessVariableService {

    @Autowired
    private IProcessVariableDao processVariableDao;

    @Autowired
    private IProcessVarInstanceDao processVarInstanceDao;

    @Override
    public int getProcessOrder(ProcessVariable bean) {

        if (bean == null || bean.getProcessDefinitionId() == null) {
            return 0;
        }
        return processVariableDao.getProcessOrder(bean);
    }

    @Override
    @Transactional
    public void addVariable(ProcessVariable... beans) {

        for (ProcessVariable bean : beans) {
            processVariableDao.save(bean);
        }
    }

    @Override
    @Transactional
    public void updateVariable(ProcessVariable... beans) {

        for (ProcessVariable bean : beans) {
            if (bean.getId() == null) {
                continue;
            }
            processVariableDao.update((ProcessVariable) bean);
        }
    }

    @Override
    @Transactional
    public void deleteVariable(ProcessVariable... beans) {

        Map<String, String> params = new HashMap<String, String>();
        for (ProcessVariable bean : beans) {
            params.put("variableId", bean.getId());
            if (bean.getId() == null) {
                continue;
            }
            this.processVarInstanceDao.deleteByVarId(bean.getId());
            this.processVariableDao.deleteById(bean.getId());
        }
    }

    @Override
    public ProcessVariable getVariableById(String id) {

        return processVariableDao.getById(id);
    }

    @Override
    public PageHelper<ProcessVariable> findProcessVariables(ProcessVariable variable, PageHelper<ProcessVariable> page) {

        return this.processVariableDao.findProcessVariables(variable,page);
    }

    @Override
    public List<ProcessVariable> findProcessVariables(ProcessVariable variable) {

        return this.processVariableDao.findProcessVariables(variable);
    }

    @Override
    public List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params) {

        return this.processVarInstanceDao.getProcessVariableInstances(params);
    }
}
