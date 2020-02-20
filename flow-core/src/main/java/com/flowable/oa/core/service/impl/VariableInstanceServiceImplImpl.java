package com.flowable.oa.core.service.impl;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizLog;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import com.flowable.oa.core.service.IVariableInstanceService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class VariableInstanceServiceImplImpl extends BaseServiceImpl<ProcessVariableInstance> implements IVariableInstanceService {

    @Override
    public Map<String, ProcessVariableInstance> getVarMap(BizInfo bizInfo, String taskId, VariableLoadType type) {

        Map<String, ProcessVariableInstance> map = new HashMap<>();
        List<ProcessVariableInstance> tList = null;
        switch (type) {
            case ALL:
                ProcessVariableInstance variableInstance = new ProcessVariableInstance();
                variableInstance.setBizId(bizInfo.getId());
                tList = this.select(variableInstance);
                break;
            case UPDATABLE:
                BizLog logBean = new BizLog();
                logBean.setBizId(bizInfo.getId());
                logBean.setTaskId(taskId);
                tList = this.loadValueByLog(logBean);
                break;
            default:
                break;
        }
        if (CollectionUtils.isNotEmpty(tList)) {
            tList.forEach(var -> map.put(var.getVariableName(), var));
        }
        return map;
    }

    @Override
    public List<ProcessVariableInstance> loadValueByLog(BizLog logBean) {

        ProcessVariableInstance instance = new ProcessVariableInstance();
        instance.setBizId(logBean.getBizId());
        instance.setTaskId(logBean.getTaskId());
        return this.select(instance);
    }
}
