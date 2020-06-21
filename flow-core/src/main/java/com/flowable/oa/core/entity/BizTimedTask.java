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
 * @description : 工单定时任务
 * @since : 2020/6/21 20:47
 */
@Data
@Entity
@Table(name = "t_timed_task")
public class BizTimedTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 64)
    private Long id;

    @Column(name = "biz_id", length = 64)
    private Long bizId;

    @Column(name = "task_name", length = 64)
    private String taskName;

    @Column(name = "task_def_key", length = 64)
    private String taskDefKey;

    @Column(name = "button_id", length = 32)
    private String buttonId;

    @Column(length = 64, name = "task_id")
    private String taskId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "end_time")
    private String endTime;

}
