package com.flowable.core.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 流程全局属性配置
 */
@Entity
@Table(name = "ACT_BIZ_PROCESS_VARIABLE")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class ProcessVariable extends AbstractVariable implements java.io.Serializable {

	private static final long serialVersionUID = 5361380519460842436L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	public String getId() {
		return super.getId();
	}

	@Column(name = "PROCESS_DEFINITION_ID", length = 64, nullable = false)
	public String getProcessDefinitionId() {
		return super.getProcessDefinitionId();
	}

	@Column(nullable = false, length = 256, name = "NAME")
	public String getName() {
		return super.getName();
	}

	@Column(nullable = false, length = 256, name = "ALIAS")
	public String getAlias() {
		return super.getAlias();
	}

	@Column(nullable = true, name = "NAME_ORDER")
	public Integer getOrder() {
		return super.getOrder();
	}

	@Column(nullable = false, name = "IS_REQUIRED")
	public Boolean isRequired() {
		return super.isRequired();
	}

	@Column(length = 256, name = "GROUP_NAME")
	public String getGroupName() {
		return super.getGroupName();
	}

	@Column(name = "GROUP_ORDER")
	public Integer getGroupOrder() {
		return super.getGroupOrder();
	}

	@Column(length = 256, name = "VIEW_COMPONENT")
	public String getViewComponent() {
		return super.getViewComponent();
	}
	
	@Override
	@Column(length = 256, name = "VIEW_PARAMS")
	public String getViewParams() {
		return super.getViewParams();
	}

	@Column(nullable = false, name = "VERSION")
	public Integer getVersion() {
		return super.getVersion();
	}

	@Column(name = "IS_PROCESS_VARIABLE")
	public Boolean isProcessVariable() {
		return processVariable;
	}

	@Override
	public void setId(String id) {
		super.setId(id);
	}

	@Override
	public void setProcessDefinitionId(String processDefinitionId) {
		super.setProcessDefinitionId(processDefinitionId);
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	@Override
	public void setAlias(String alias) {
		super.setAlias(alias);
	}

	@Override
	public void setOrder(Integer order) {
		super.setOrder(order);
	}

	@Override
	public void setRequired(Boolean required) {
		super.setRequired(required);
	}

	@Override
	public void setProcessVariable(Boolean processVariable) {
		super.setProcessVariable(processVariable);
	}

	@Override
	public void setGroupName(String groupName) {
		super.setGroupName(groupName);
	}

	@Override
	public void setGroupOrder(Integer groupOrder) {
		super.setGroupOrder(groupOrder);
	}

	@Override
	public void setViewComponent(String viewComponent) {
		super.setViewComponent(viewComponent);
	}

	@Override
	public void setViewParams(String viewParams) {
		super.setViewParams(viewParams);
	}

	@Override
	public void setVersion(Integer version) {
		super.setVersion(version);
	}
	@Column(length = 256, name = "VIEW_DATAS")
	public String getViewDatas() {
		return super.getViewDatas();
	}
	
	@Override
	public void setViewDatas(String viewDatas) {
		super.setViewDatas(viewDatas);
	}
	
	@Column(length = 64, name = "REF_VARIABLE")
	public String getRefVariable() {
		return super.getRefVariable();
	}

	public void setRefVariable(String refVariable) {
		super.setRefVariable(refVariable);
	}

	@Column(length = 256, name = "REF_PARAM")
	public String getRefParam() {
		return super.getRefParam();
	}

	public void setRefParam(String refParam) {
		super.setRefParam(refParam);
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
