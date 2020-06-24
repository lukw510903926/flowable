package com.flowable.oa.core.service;

import com.flowable.oa.core.entity.ProcessTaskAssignee;
import com.flowable.oa.core.util.mybatis.IBaseService;
import com.github.pagehelper.PageInfo;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020-06-21 21:06
 */
public interface IProcessTaskAssigneeService extends IBaseService<ProcessTaskAssignee> {

    /**
     * 获取任务待办人
     *
     * @param processTaskAssignee
     * @return
     */
    ProcessTaskAssignee getTaskAssignee(ProcessTaskAssignee processTaskAssignee);



    /**
     * 获取任务待办人
     *
     * @param processDefinitionId
     * @param taskDefKey
     * @return
     */
    ProcessTaskAssignee getTaskAssignee(String processDefinitionId,String taskDefKey );
    /**
     * 查询任务待办人列表
     *
     * @param pageInfo
     * @param processTaskAssignee
     * @return
     */
    PageInfo<ProcessTaskAssignee> queryList(PageInfo<ProcessTaskAssignee> pageInfo, ProcessTaskAssignee processTaskAssignee);
}
