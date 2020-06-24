package com.flowable.oa.core.service.impl;

import com.flowable.oa.core.dao.ProcessTaskAssigneeMapper;
import com.flowable.oa.core.entity.ProcessTaskAssignee;
import com.flowable.oa.core.service.IProcessTaskAssigneeService;
import com.flowable.oa.core.util.PageUtil;
import com.flowable.oa.core.util.mybatis.BaseServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020-06-21 21:07
 */
@Slf4j
@Service
public class ProcessTaskAssigneeServiceImpl extends BaseServiceImpl<ProcessTaskAssignee> implements IProcessTaskAssigneeService {

    @Resource
    private ProcessTaskAssigneeMapper processTaskAssigneeMapper;

    @Override
    public void saveOrUpdate(ProcessTaskAssignee entity) {
        super.saveOrUpdate(entity);
    }

    @Override
    public ProcessTaskAssignee getTaskAssignee(ProcessTaskAssignee processTaskAssignee) {

        List<ProcessTaskAssignee> list = processTaskAssigneeMapper.select(processTaskAssignee);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public ProcessTaskAssignee getTaskAssignee(String processDefinitionId, String taskDefKey) {
        ProcessTaskAssignee processTaskAssignee = new ProcessTaskAssignee();
        processTaskAssignee.setProcessDefinitionId(processDefinitionId);
        processTaskAssignee.setTaskDefKey(taskDefKey);
        return this.getTaskAssignee(processTaskAssignee);
    }

    @Override
    public PageInfo<ProcessTaskAssignee> queryList(PageInfo<ProcessTaskAssignee> pageInfo, ProcessTaskAssignee processTaskAssignee) {
        PageUtil.startPage(pageInfo);
        List<ProcessTaskAssignee> select = this.processTaskAssigneeMapper.select(processTaskAssignee);
        return PageInfo.of(select);
    }
}
