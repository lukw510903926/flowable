package com.flowable.oa.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowable.oa.util.mybatis.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.BizLog;
import com.flowable.oa.entity.ProcessVariableInstance;
import com.flowable.oa.service.IVariableInstanceService;
import com.flowable.oa.util.Constants;

@Service
public class VariableInstanceServiceImplImpl extends BaseServiceImpl<ProcessVariableInstance> implements IVariableInstanceService {

    @Override
    @Transactional
    public void addProcessInstance(ProcessVariableInstance... beans) {

        for (ProcessVariableInstance bean : beans) {
            this.save(bean);
        }
    }

    @Override
    public void updateProcessInstance(ProcessVariableInstance... beans) {

        for (ProcessVariableInstance bean : beans) {
            if (bean.getId() == null) {
                continue;
            }
            this.updateNotNull(bean);
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

    	ProcessVariableInstance instance = new ProcessVariableInstance();
    	instance.setBizId(bean.getId());
        return this.findByModel(instance, false);
    }

    /**
     * 加载某个工单所填写的所有数据
     * @param instance
     * @param isLike
     * @return
     */
    @Override
    public List<ProcessVariableInstance> findVariableInstances(ProcessVariableInstance instance,boolean isLike) {

        return this.findByModel(instance, isLike);
    }

    @Override
    public Map<String, ProcessVariableInstance> getVarMap(BizInfo bizInfo, String taskId, VariableLoadType type) {

        Map<String, ProcessVariableInstance> map = new HashMap<>();
        BizLog logBean = new BizLog();
        logBean.setBizId(bizInfo.getId());
        logBean.setTaskID(Constants.TASK_START);
        List<ProcessVariableInstance> pList = this.loadValueByLog(logBean);
        List<ProcessVariableInstance> tList = null;
        switch (type) {
            case ALL:
                tList = loadInstances(bizInfo);
                break;
            case UPDATABLE:
                logBean.setTaskID(taskId);
                tList = this.loadValueByLog(logBean);
                break;
        }
        if (!CollectionUtils.isEmpty(pList)) {
            pList.forEach(var -> map.put(var.getVariableName(), var));
        }
        if (!CollectionUtils.isEmpty(tList)) {
            tList.forEach(var -> map.put(var.getVariableName() + (type == VariableLoadType.ALL ? ('-' + taskId) : ""), var));
        }
        return map;
    }

    @Override
    public List<ProcessVariableInstance> loadValueByLog(BizLog logBean) {
    	
    	ProcessVariableInstance instance = new ProcessVariableInstance();
    	instance.setBizId(logBean.getBizId());
    	instance.setTaskId(logBean.getTaskID());
    	return this.findByModel(instance, false);
    }
}
