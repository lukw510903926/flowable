package com.flowable.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 流程全局属性配置
 */
@Entity
@Table(name = "T_BIZ_PROCESS_VARIABLE")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ProcessVariable implements java.io.Serializable {

	private static final long serialVersionUID = 5361380519460842436L;

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;

	/**
	 * 任务Id
	 */
	@Column(name = "TASK_ID", length = 32)
	private String taskId;

	/**
	 * 流程模板ID
	 */
	@Column(name = "PROCESS_DEFINITION_ID", length = 64, nullable = false)
	private String processDefinitionId;

	/**
	 * 属性中文名
	 */
	@Column(nullable = false, length = 256, name = "NAME")
	private String name;

	/**
	 * 属性别名
	 */
	@Column(nullable = false, length = 256, name = "ALIAS")
	private String alias;

	/**
	 * 属性排序
	 */

	@Column(nullable = true, name = "NAME_ORDER")
	private Integer order;

	/**
	 * 是否必填
	 */
	@Column(nullable = false, name = "IS_REQUIRED")
	private Boolean required;

	/**
	 * 是否为流程变量 是提交时传递到下个节点
	 */
	@Column(name = "IS_PROCESS_VARIABLE")
	private Boolean processVariable;

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
	 * 所属版本
	 */
	@Column(nullable = false, name = "VERSION")
	private Integer version;

	/**
	 * 联动属性
	 */
	@Column(length = 64, name = "REF_VARIABLE")
	private String refVariable;

	/**
	 * 联动属性值
	 */
	@Column(length = 256, name = "REF_PARAM")
	private String refParam;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Boolean getRequired() {
		return required;
	}

	public Boolean getProcessVariable() {
		return processVariable;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Boolean isRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean isProcessVariable() {
		return processVariable;
	}

	public void setProcessVariable(Boolean processVariable) {
		this.processVariable = processVariable;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(Integer groupOrder) {
		this.groupOrder = groupOrder;
	}

	public String getViewComponent() {
		return viewComponent;
	}

	public void setViewComponent(String viewComponent) {
		this.viewComponent = viewComponent;
	}

	public String getViewParams() {
		return viewParams;
	}

	public void setViewParams(String viewParams) {
		this.viewParams = viewParams;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getViewDatas() {
		return viewDatas;
	}

	public void setViewDatas(String viewDatas) {
		this.viewDatas = viewDatas;
	}
	
	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public String getRefVariable() {
		return refVariable;
	}

	public void setRefVariable(String refVariable) {
		this.refVariable = refVariable;
	}

	public String getRefParam() {
		return refParam;
	}

	public void setRefParam(String refParam) {
		this.refParam = refParam;
	}

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
