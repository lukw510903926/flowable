package com.flowable.oa.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowable.oa.core.entity.BizInfo;
import com.flowable.oa.core.entity.BizLog;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import com.flowable.oa.core.service.IVariableInstanceService;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class VariableInstanceServiceImplImpl extends BaseServiceImpl<ProcessVariableInstance> implements IVariableInstanceService {

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
        return this.select(instance);
    }

    @Override
    public Map<String, ProcessVariableInstance> getVarMap(BizInfo bizInfo, String taskId, VariableLoadType type) {

        Map<String, ProcessVariableInstance> map = new HashMap<>();
        BizLog logBean = new BizLog();
        logBean.setBizId(bizInfo.getId());
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
        if (CollectionUtils.isNotEmpty(tList)) {
            tList.forEach(var -> map.put(var.getVariableName(), var));
        }
        return map;
    }

    @Override
    public List<ProcessVariableInstance> loadValueByLog(BizLog logBean) {
    	
    	ProcessVariableInstance instance = new ProcessVariableInstance();
    	instance.setBizId(logBean.getBizId());
    	instance.setTaskId(logBean.getTaskID());
    	return this.select(instance);
    }
}
