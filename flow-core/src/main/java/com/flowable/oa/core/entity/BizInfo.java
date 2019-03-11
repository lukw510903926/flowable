package com.flowable.oa.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工单对象
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:31:34
 * @description :
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "T_BIZ_INFO")
public class BizInfo implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = -9003521142344551524L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, length = 64, name = "ID")
    private Integer id;

    /**
     * 工单号
     */
    @Column(length = 64, name = "WORK_NUM")
    private String workNum;

    @Column(length = 512, name = "TITLE")
    private String title;

    @Column(length = 256, name = "BIZ_TYPE")
    private String bizType;

    @Column(length = 64, name = "PROCESS_DEFINITION_ID")
    private String processDefinitionId;

    @Column(length = 64, name = "PROCESS_INSTANCE_ID")
    private String processInstanceId;

    @Column(length = 256, name = "TASK_ID")
    private String taskId;

    @Column(length = 64, name = "TASK_DEF_KEY")
    private String taskDefKey;

    @Column(length = 256, name = "TASK_NAME")
    private String taskName;

    /**
     * 当前任务处理人
     */
    @Column(length = 256, name = "TASK_ASSIGNEE")
    private String taskAssignee;

    @Column(length = 256, name = "CREATE_USER")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(length = 32, name = "BIZ_STATUS")
    private String status;

    @Column(length = 128, name = "SOURCE")
    private String source;

    @Column(length = 64, name = "PARENT_ID")
    private Integer parentId;

    @Column(length = 256, name = "PARENT_TASKNAME")
    private String parentTaskName;

    public BizInfo clone() {
        BizInfo bizInfo = null;
        try {
            bizInfo = (BizInfo) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bizInfo;
    }

}
