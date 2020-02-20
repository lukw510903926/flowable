package com.flowable.oa.core.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * 工单定时任务
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:32:45
 * @description :
 */
@Data
@Entity
@Table(name = "t_timed_task")
public class BizTimedTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 64)
    private Integer id;

    @Column(name = "biz_id", length = 64)
    private Integer bizId;

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
