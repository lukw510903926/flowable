package com.flowable.oa.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 流程日志
 */
@Data
@Entity
@Table(name = "t_biz_log")
public class BizLog implements java.io.Serializable {

    private static final long serialVersionUID = -67861329386846521L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Integer id;

    @Column(name = "biz_id")
    private Integer bizId;

    @Column(length = 512, name = "task_name")
    private String taskName;

    @Column(nullable = false, length = 64, name = "task_id")
    private String taskId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Column(nullable = false, length = 256, name = "handle_user")
    private String handleUser;

    @Column(name = "handle_user_name")
    private String handleUserName;

    @Column(length = 64, name = "user_phone")
    private String userPhone;

    @Column(length = 64, name = "user_dept")
    private String userDept;

    @Column(length = 1000, name = "handle_description")
    private String handleDescription;

    @Column(nullable = false, length = 512, name = "handle_result")
    private String handleResult;

    @Column(nullable = false, length = 512, name = "handle_name")
    private String handleName;
}
