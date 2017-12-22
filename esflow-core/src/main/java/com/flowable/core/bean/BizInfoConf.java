package com.flowable.core.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 2622
 * @time 2016年5月28日
 * @email lukw@eastcom-sw.com
 */

@Entity
@Table(name = "ACT_BIZ_INFO_CONF")
@DynamicInsert
@DynamicUpdate
public class BizInfoConf implements Serializable,Cloneable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	
	@Column(nullable = true, length = 64, name = "TASK_ID")
	private String taskId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();
	
	public BizInfoConf() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 当前任务处理人
	 */
	@Column(nullable = true, length = 256, name = "TASK_ASSIGNEE")
	private String taskAssignee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIZ_ID")
	private BizInfo bizInfo;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskAssignee() {
		return taskAssignee;
	}

	public void setTaskAssignee(String taskAssignee) {
		this.taskAssignee = taskAssignee;
	}

	public BizInfo getBizInfo() {
		return bizInfo;
	}

	public void setBizInfo(BizInfo bizInfo) {
		this.bizInfo = bizInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public BizInfoConf clone() {
		BizInfoConf bizInfoConf = null;
		try {
			bizInfoConf = (BizInfoConf) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bizInfoConf;
	}
}
