package com.flowable.oa.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 流程日志
 */
@Data
@Entity
@Table(name = "T_BIZ_LOG")
public class BizLog implements java.io.Serializable {

	private static final long serialVersionUID = -67861329386846521L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private Integer id;

	@Column(name = "BIZ_ID")
	private Integer bizId;

	@Column(length = 512, name = "TASK_NAME")
	private String taskName;

	@Column(nullable = false, length = 64, name = "TASK_ID")
	private String taskID;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(nullable = false, length = 256, name = "HANDLE_USER")
	private String handleUser;

	@Column(name = "HANDLE_USER_NAME")
	private String handleUserName;
	
	@Column(length = 64, name = "USER_PHONE")
	private String userPhone;
	
	@Column(length = 64, name = "USER_DEPT")
	private String userDept;

	@Column(length = 1000, name = "HANDLE_DESCRIPTION")
	private String handleDescription;

	@Column(nullable = false, length = 512, name = "HANDLE_RESULT")
	private String handleResult;

	@Column(nullable = false, length = 512, name = "HANDLE_NAME")
	private String handleName;
}
