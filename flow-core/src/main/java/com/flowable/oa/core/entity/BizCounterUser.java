package com.flowable.oa.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会签人员列表
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/22 18:19
 **/
@Data
@Entity
@Table(name = "t_biz_counter_user")
public class BizCounterUser implements Serializable {

    private static final long serialVersionUID = 8899924192856670854L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    @Column(name = "user_name", length = 32)
    private String username;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "biz_id", length = 32)
    private String bizId;

    @Column(name = "task_id", length = 32)
    private String taskId;

    @Column(name = "create_time")
    private Date createTime;
}
