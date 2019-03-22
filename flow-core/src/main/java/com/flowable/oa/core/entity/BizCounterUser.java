package com.flowable.oa.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会签人员列表
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/22 18:19
 **/
@Data
@Entity
@Table(name = "T_BIZ_COUNTER_USER")
public class BizCounterUser implements Serializable {

    private static final long serialVersionUID = 8899924192856670854L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "ID")
    private Integer id;

    @Column(name = "USER_NAME", length = 32)
    private String username;

    @Column(name = "NAME", length = 32)
    private String name;

    @Column(name = "DEPARTMENT", length = 32)
    private String department;

    @Column(name = "BIZ_ID", length = 32)
    private String bizId;

    @Column(name = "TASK_ID", length = 32)
    private String taskId;

    @Column(name = "CREATE_TIME")
    private Date createTime ;
}
