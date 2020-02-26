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
import javax.persistence.Transient;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.Order;

/**
 * <p>
 * 工单对象
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/22 18:17
 **/
@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_biz_info")
public class BizInfo implements Serializable, Cloneable {

    private static final long serialVersionUID = -9003521142344551524L;

    @Id
    @Order("desc")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    /**
     * 工单号
     */
    @Column(length = 64, name = "work_num")
    private String workNum;

    @Column(length = 512, name = "title")
    private String title;

    @Column(length = 256, name = "biz_type")
    private String bizType;

    @Column(length = 64, name = "process_definition_id")
    private String processDefinitionId;

    @Column(length = 64, name = "process_instance_id")
    private String processInstanceId;

    @Column(length = 64, name = "task_def_key")
    private String taskDefKey;

    @Column(length = 256, name = "task_name")
    private String taskName;

    /**
     * 当前任务处理人
     */
    @Transient
    private String taskAssignee;

    @Transient
    private String taskId;

    @Column(length = 256, name = "create_user")
    private String createUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Column(length = 32, name = "biz_status")
    private String status;

    @Column(length = 128, name = "source")
    private String source;

    @Column(length = 64, name = "parent_id")
    private Long parentId;

    @Override
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
