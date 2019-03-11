package com.flowable.oa.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:31:53
 * @description :
 */
@Data
@Entity
@Table(name = "T_BIZ_INFO_CONF")
public class BizInfoConf implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "ID")
    private Integer id;

    @Column(name = "BIZ_ID")
    private Integer bizId;

    @Column(length = 64, name = "TASK_ID")
    private String taskId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime = new Date();

    /**
     * 当前任务处理人
     */
    @Column(length = 256, name = "TASK_ASSIGNEE")
    private String taskAssignee;

    /**
     * 角色
     */
    @Transient
    private Set<String> roles;

    @Transient
    private String loginUser;

    public BizInfoConf clone() {

        BizInfoConf bizInfoConf = null;
        try {
            bizInfoConf = (BizInfoConf) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bizInfoConf;
    }
}
