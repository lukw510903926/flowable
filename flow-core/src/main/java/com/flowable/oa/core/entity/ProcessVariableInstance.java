package com.flowable.oa.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "t_biz_process_instance")
public class ProcessVariableInstance implements Serializable, Cloneable {

    private static final long serialVersionUID = 620831623030964444L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Integer id;

    /**
     * 增加填写时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 参数
     */
    @Column(name = "process_variable_id")
    private Integer variableId;

    /**
     * 流程id
     */
    @Column(name = "biz_id", length = 64)
    private Integer bizId;

    /**
     * 任务ID
     */
    @Column(name = "task_id", length = 32)
    private String taskId;

    /**
     * 流程实例ID
     */
    @Column(length = 64, name = "process_instance_id")
    private String processInstanceId;

    /**
     * 值
     */
    @Column(nullable = false, length = 512, name = "value")
    private String value;

    /**
     * 参数名称
     */
    @Column(name = "variable_name", length = 32)
    private String variableName;

    @Column(name = "handle_user", length = 64)
    private String handleUser;

    /**
     * 参数别名
     */
    @Column(name = "variable_alias", length = 32)
    private String variableAlias;

    @Column(name = "view_component")
    private String viewComponent;

    @Override
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
