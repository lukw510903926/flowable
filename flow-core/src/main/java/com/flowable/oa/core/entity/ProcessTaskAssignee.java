package com.flowable.oa.core.entity;

import com.flowable.oa.core.constants.HandleTypeEnum;
import lombok.Data;
import tk.mybatis.mapper.annotation.Order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description : 任务待办人
 * @since : 2020/6/21 20:28
 */
@Data
@Entity
@Table(name = "t_biz_process_task_assignee")
public class ProcessTaskAssignee {

    @Id
    @Order("desc")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    /**
     * 流程定义ID
     */
    @Column(length = 64, name = "process_definition_id")
    private String processDefinitionId;

    /**
     * 流程明细
     */
    @Column(length = 128, name = "process_name")
    private String processName;

    /**
     * 任务定义key
     */
    @Column(length = 64, name = "task_def_key")
    private String taskDefKey;

    /**
     * 任务名称
     */
    @Column(length = 256, name = "task_name")
    private String taskName;

    /**
     * 任务处理人
     */
    @Column(length = 128, name = "task_assignee")
    private String taskAssignee;

    /**
     * 代办类型
     *
     * @see HandleTypeEnum
     */
    @Column(length = 4, name = "handle_type")
    private Integer handleType;
}
