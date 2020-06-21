package com.flowable.oa.core.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description : 流程全局属性配置
 * @since : 2020/6/21 20:46
 */
@Data
@Entity
@Table(name = "t_biz_process_variable")
public class ProcessVariable implements Serializable, Cloneable {

    private static final long serialVersionUID = 5361380519460842436L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, length = 64, name = "id")
    private Long id;

    /**
     * 任务Id
     */
    @Column(name = "task_id", length = 32)
    private String taskId;

    /**
     * 流程模板ID
     */
    @Column(name = "process_definition_id", length = 64)
    private String processDefinitionId;

    /**
     * 属性中文名
     */
    @Column(length = 256, name = "name")
    private String name;

    /**
     * 属性别名
     */
    @Column(length = 256, name = "alias")
    private String alias;

    /**
     * 属性排序
     */

    @Column(name = "name_order")
    private Integer order;

    /**
     * 是否必填
     */
    @Column(name = "is_required")
    private Boolean isRequired;

    /**
     * 是否为流程变量 是提交时传递到下个节点
     */
    @Column(name = "is_process_variable")
    private Boolean isProcessVariable;

    /**
     * 分组名
     */
    @Column(length = 256, name = "group_name")
    private String groupName;

    /**
     * 分组排序
     */
    @Column(name = "group_order")
    private Integer groupOrder;

    /**
     * 页面组件
     */
    @Column(length = 256, name = "view_component")
    private String viewComponent;

    /**
     * 页面组件数据
     */
    @Column(length = 256, name = "view_datas")
    private String viewDatas;

    /**
     * 下拉组件数据URL
     */
    @Column(length = 256, name = "view_url")
    private String viewUrl;

    /**
     * 页面组件参数
     */
    @Column(length = 256, name = "view_params")
    private String viewParams;

    /**
     * 联动属性
     */
    @Column(length = 64, name = "ref_variable")
    private Long refVariable;

    /**
     * 联动属性值
     */
    @Column(length = 256, name = "ref_param")
    private String refParam;

    @Override
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
