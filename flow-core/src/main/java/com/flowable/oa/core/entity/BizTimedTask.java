package com.flowable.oa.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 工单定时任务
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:32:45
 * @author : lukewei
 * @description :
 */
@Data
@Entity
@Table(name="T_TIMED_TASK")
public class BizTimedTask implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	private Integer id;
	
	@Column(name="BIZ_ID",length = 64)
	private Integer bizId;
	
	@Column(name="TASK_NAME",length = 64)
	private String taskName;
	
	@Column(name="TASK_DEF_KEY",length = 64)
	private String taskDefKey;
	
	@Column(name="BUTTON_ID", length = 32)
	private String buttonId;
	
	@Column(length = 64, name = "TASK_ID")
	private String taskId;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="END_TIME")
	private String endTime ;

}
