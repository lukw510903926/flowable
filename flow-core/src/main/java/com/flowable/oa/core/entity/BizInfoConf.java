package com.flowable.oa.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowable.oa.core.constants.HandleTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description : 工单任务待办人
 * @since : 2020/6/21 20:48
 */
@Data
@Entity
@Table(name = "t_biz_info_conf")
public class BizInfoConf implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    @Column(name = "biz_id")
    private Long bizId;

    @Column(length = 64, name = "task_id")
    private String taskId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 当前任务处理人
     */
    @Column(length = 256, name = "task_assignee")
    private String taskAssignee;

    /**
     * 代办类型
     *
     * @see HandleTypeEnum
     */
    @Column(length = 4, name = "handle_type")
    private Integer handleType;

    /**
     * 角色
     */
    @Transient
    private Set<String> roles;

    @Transient
    private String loginUser;

    @Override
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
