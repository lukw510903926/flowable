package com.flowable.oa.core.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :会签任务
 * @since : 2020/6/21 20:47
 */
@Data
@Entity
@Table(name = "t_counter_sign")
public class Countersign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, length = 64, name = "id")
    private Long id;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "biz_id")
    private String bizId;

    @Column(name = "processinstance_id")
    private String processInstanceId;

    @Column(name = "processdefinition_id")
    private String processDefinitionId;

    @Column(name = "task_assignee")
    private String taskAssignee;

    @Column(name = "result_type")
    private Integer resultType;

    /**
     * 当前会签是否结束 0 没有,1 结束
     */
    @Column(name = "is_complete")
    private Integer isComplete = 0;

    @Column(name = "create_time")
    private Date createTime;

}
