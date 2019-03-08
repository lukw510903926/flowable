package com.flowable.oa.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:30:48
 * @author : lukewei
 * @description :
 */
@Data
@Entity
@Table(name="T_BIZ_INFO_DELAY_TIME")
public class ActBizInfoDelayTime implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	@Column(length = 64, name = "TASK_ID")
	private String taskId;
	
	@Column(length = 64, name = "BIZ_ID")
	private String bizId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELAY_TIME")
	private Date delayTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@Column(length = 10, name = "APPLY_STATUS")
	private Integer applyStatus = 0;
	
	@Column(length = 32, name = "TASK_NAME")
	private String taskName;
}
