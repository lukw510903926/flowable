package com.flowable.oa.core.entity;

import java.io.Serializable;
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

/**
 * 流程全局实例（存储具体填写的值）
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:34:09
 * @description :
 */
@Data
@Entity
@Table(name = "T_BIZ_PROCESS_INSTANCE")
public class ProcessVariableInstance implements Serializable, Cloneable {

    private static final long serialVersionUID = 620831623030964444L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, length = 64, name = "ID")
    private String id;

    /**
     * 增加填写时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 参数
     */
    @Column(name = "PROCESS_VARIABLE_ID")
    private String variableId;

    /**
     * 流程id
     */
    @Column(name = "BIZ_ID", length = 64)
    private String bizId;

    /**
     * 任务ID
     */
    @Column(name = "TASK_ID", length = 32)
    private String taskId;

    /**
     * 流程实例ID
     */
    @Column(length = 64, name = "PROCESS_INSTANCE_ID")
    private String processInstanceId;

    /**
     * 值
     */
    @Column(nullable = false, length = 512, name = "VALUE")
    private String value;

    /**
     * 参数名称
     */
    @Column(name = "VARIABLE_NAME", length = 32)
    private String variableName;

    @Column(name = "HANDLE_USER", length = 64)
    private String handleUser;

    /**
     * 参数别名
     */
    @Column(name = "VARIABLE_ALIAS", length = 32)
    private String variableAlias;

    @Column(name = "VIEW_COMPONENT")
    private String viewComponent;

    public ProcessVariableInstance clone() {
        ProcessVariableInstance instance = null;
        try {
            instance = (ProcessVariableInstance) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

}
