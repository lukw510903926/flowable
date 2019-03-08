package com.flowable.oa.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 会签人员列表
 * 2016年8月23日
 * @author lukw 
 * 下午8:00:49
 * com.eastcom.esflow.bean
 * @email lukw@eastcom-sw.com
 */
@Data
@Entity
@Table(name="T_BIZ_COUNTER_USER")
public class BizCounterUser implements Serializable{

	private static final long serialVersionUID = 8899924192856670854L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	@Column(name="USER_NAME",length=32)
	private String username;
	
	@Column(name="NAME" ,length=32)
	private String name;
	
	@Column(name="DEPTMENT_NAME",length = 32)
	private String deptmentName;
	
	@Column(name="BIZ_ID",length = 32)
	private String bizId;
	
	@Column(name="TASK_ID" ,length=32)
	private String taskId;
	
	@Column(name="CREATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime = new Date();
}
