package com.flowable.core.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 会签人员列表
 * 2016年8月23日
 * @author lukw 
 * 下午8:00:49
 * com.eastcom.esflow.bean
 * @email lukw@eastcom-sw.com
 */
@Entity
@Table(name="BIZ_COUNTER_USER")
public class BizCounterUser implements Serializable{

	private static final long serialVersionUID = 8899924192856670854L;
	
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	@Column(name="USER_NAME",length=32)
	private String username;
	
	@Column(name="NAME" ,length=32)
	private String name;
	
	@Column(name="DEPTMENT_NAME",length = 32)
	private String deptmentName;
	
	@Column(name="BIZID",length = 32)
	private String bizId;
	
	@Column(name="TASKID" ,length=32)
	private String taskId;
	
	@Column(name="CREATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptmentName() {
		return deptmentName;
	}

	public void setDeptmentName(String deptmentName) {
		this.deptmentName = deptmentName;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
