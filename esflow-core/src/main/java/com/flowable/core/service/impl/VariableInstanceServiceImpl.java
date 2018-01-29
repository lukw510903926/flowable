package com.flowable.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowable.core.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.bean.BizLog;
import com.flowable.core.bean.ProcessVariableInstance;
import com.flowable.core.dao.IProcessVarInstanceDao;
import com.flowable.core.service.IVariableInstanceService;
import org.springframework.util.CollectionUtils;

@Service
public class VariableInstanceServiceImpl implements IVariableInstanceService {

    @Autowired
    private IProcessVarInstanceDao processInstanceDao;

    @Override
    @Transactional
    public void addProcessInstance(ProcessVariableInstance... beans) {

        for (ProcessVariableInstance bean : beans) {
            processInstanceDao.save(bean);
        }
    }

    @Override
    public void updateProcessInstance(ProcessVariableInstance... beans) {

        for (ProcessVariableInstance bean : beans) {
            if (bean.getId() == null) {
                continue;
            }
            processInstanceDao.update(bean);
        }
    }

    /**
     * 加载某个工单所填写的所有数据
     *
     * @param bean
     * @return
     * @
     */
    @Override
    public List<ProcessVariableInstance> loadInstances(BizInfo bean) {

        return processInstanceDao.loadProcessInstancesByBizId(bean.getId());
    }

    @Override
    public Map<String, ProcessVariableInstance> getVarMap(BizInfo bizInfo, String taskId, VariableLoadType type) {

        Map<String, ProcessVariableInstance> map = new HashMap<String, ProcessVariableInstance>();
        BizLog logBean = new BizLog();
        logBean.setBizInfo(bizInfo);
        logBean.setTaskID(Constants.TASK_START);
        List<ProcessVariableInstance> pList = this.loadValueByLog(logBean);
        List<ProcessVariableInstance> tList = null;
        switch (type) {
            case ALL:
                tList = processInstanceDao.findByProcInstId(bizInfo.getProcessInstanceId());
                break;
            case UPDATABLE:
                logBean.setTaskID(taskId);
                tList = this.loadValueByLog(logBean);
                break;
        }
        if (!CollectionUtils.isEmpty(pList)) {
            pList.forEach(var -> map.put(var.getVariable().getName(), var));
        }
        if (!CollectionUtils.isEmpty(tList)) {
            tList.forEach(var -> map.put(var.getVariable().getName() + (type == VariableLoadType.ALL ? ('-' + taskId) : ""), var));
        }
        return map;
    }

    @Override
    public List<ProcessVariableInstance> loadValueByLog(BizLog logBean) {

        return processInstanceDao.loadValueByLog(logBean);
    }
}
