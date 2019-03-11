package com.flowable.oa.core.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 流程全局属性配置
 */
@Data
@Entity
@Table(name = "T_BIZ_PROCESS_VARIABLE")
public class ProcessVariable implements Serializable, Cloneable {

    private static final long serialVersionUID = 5361380519460842436L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, length = 64, name = "ID")
    private Integer id;

    /**
     * 任务Id
     */
    @Column(name = "TASK_ID", length = 32)
    private String taskId;

    /**
     * 流程模板ID
     */
    @Column(name = "PROCESS_DEFINITION_ID", length = 64)
    private String processDefinitionId;

    /**
     * 属性中文名
     */
    @Column(length = 256, name = "NAME")
    private String name;

    /**
     * 属性别名
     */
    @Column(length = 256, name = "ALIAS")
    private String alias;

    /**
     * 属性排序
     */

    @Column(name = "NAME_ORDER")
    private Integer order;

    /**
     * 是否必填
     */
    @Column(name = "IS_REQUIRED")
    private Boolean isRequired;

    /**
     * 是否为流程变量 是提交时传递到下个节点
     */
    @Column(name = "IS_PROCESS_VARIABLE")
    private Boolean isProcessVariable;

    /**
     * 分组名
     */
    @Column(length = 256, name = "GROUP_NAME")
    private String groupName;

    /**
     * 分组排序
     */
    @Column(name = "GROUP_ORDER")
    private Integer groupOrder;

    /**
     * 页面组件
     */
    @Column(length = 256, name = "VIEW_COMPONENT")
    private String viewComponent;

    /**
     * 页面组件数据
     */
    @Column(length = 256, name = "VIEW_DATAS")
    private String viewDatas;

    /**
     * 下拉组件数据URL
     */
    @Column(length = 256, name = "VIEW_URL")
    private String viewUrl;

    /**
     * 页面组件参数
     */
    @Column(length = 256, name = "VIEW_PARAMS")
    private String viewParams;

    /**
     * 联动属性
     */
    @Column(length = 64, name = "REF_VARIABLE")
    private Integer refVariable;

    /**
     * 联动属性值
     */
    @Column(length = 256, name = "REF_PARAM")
    private String refParam;

    public ProcessVariable clone() {
        ProcessVariable instance = null;
        try {
            instance = (ProcessVariable) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}
