package com.flowable.oa.core.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/22 22:43
 **/
@Data
@EqualsAndHashCode
public class BizInfoVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -9003521142344551524L;

    /**
     * 订单ID
     */
    private Integer id;

    /**
     * 工单号
     */
    private String workNum;

    /**
     * 工单标题
     */
    private String title;

    /**
     * 工单类型
     */
    private String bizType;

    /**
     * 工单定义key
     */
    private String processDefinitionId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 当前任务id
     */
    private String taskId;

    /**
     * 当前任务定义key
     */
    private String taskDefKey;

    /**
     * 当前任务名称
     */
    private String taskName;

    /**
     * 当前任务处理人
     */
    private String taskAssignee;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date createTime2;

    /**
     * 工单状态
     */
    private String status;

    /**
     * 父工单id
     */
    private String parentId;

    /**
     * 创建子单时父工单的任务名称
     */
    private String parentTaskName;

    /**
     * 查询方式 temp 草稿状态, work 我的待办 handle 我的已处理 all 全部工单 create 我创建的工单
     */
    private String action;
}
