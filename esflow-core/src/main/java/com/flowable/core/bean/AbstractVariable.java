package com.flowable.core.bean;

public class AbstractVariable implements Cloneable {

	/**
	 * ID
	 */
	protected String id;

	/**
	 * 流程模板ID
	 */
	protected String processDefinitionId;

	/**
	 * 属性中文名
	 */
	protected String name;

	/**
	 * 属性别名
	 */
	protected String alias;

	/**
	 * 属性排序
	 */
	protected Integer order;

	/**
	 * 是否必填
	 */
	protected Boolean required;

	/**
	 * 是否为流程变量
	 */
	protected Boolean processVariable;

	/**
	 * 分组名
	 */
	protected String groupName;

	/**
	 * 分组排序
	 */
	protected Integer groupOrder;

	/**
	 * 页面组件
	 */
	protected String viewComponent;
	
	/**
	 * 页面组件数据
	 */
	protected String viewDatas;

	/**
	 * 页面组件参数
	 */
	protected String viewParams;

	/**
	 * 所属版本
	 */
	protected Integer version;
	
	/**
	 * 联动属性
	 */
	protected String refVariable;
	
	/**
	 * 联动属性值
	 */
	protected String refParam;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	public AbstractVariable clone() {
		AbstractVariable instance = null;
		try {
			instance = (AbstractVariable) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
}
