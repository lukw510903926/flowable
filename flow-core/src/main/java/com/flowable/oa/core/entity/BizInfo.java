package com.flowable.oa.core.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

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
@Table(name = "T_BIZ_INFO")
public class BizInfo implements Serializable, Cloneable {

    private static final long serialVersionUID = -9003521142344551524L;

    @Id
    @Order("desc")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "ID")
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

    @Column(length = 64, name = "TASK_DEF_KEY")
    private String taskDefKey;

    @Column(length = 256, name = "TASK_NAME")
    private String taskName;

    /**
     * 当前任务处理人
     */
    @Transient
    private String taskAssignee;

    @Transient
    private String taskId;

    @Column(length = 256, name = "CREATE_USER")
    private String createUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(length = 32, name = "BIZ_STATUS")
    private String status;

    @Column(length = 128, name = "SOURCE")
    private String source;

    @Column(length = 64, name = "PARENT_ID")
    private Integer parentId;

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
