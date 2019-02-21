package com.flowable.oa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 会签任务
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:32:58
 * @author : lukewei
 * @description :
 */
@Data
@Entity
@Table(name="T_COUNTER_SIGN")
public class Countersign implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	@Column(name="TASK_ID", length = 32, nullable = false)
	private String taskId;

	@Column(name="BIZ_ID", length = 32, nullable = false)
	private String bizId;
	
	@Column(name="PROCESSINSTANCE_ID", length = 10, nullable = false)
	private String processInstanceId;
	
	@Column(name="PROCESSDEFINITION_ID", length = 128, nullable = false)
	private String processDefinitionId;
	
	@Column(name="TASK_ASSIGNEE", length = 32, nullable = false)
	private String taskAssignee;
	
	@Column(name="RESULT_TYPE", length = 10, nullable = false)
	private Integer resultType;
	
	@Column(name="IS_COMPLETE", length = 10, nullable = false)
	private Integer isComplete = 0;//当前会签是否结束 0 没有,1 结束
	
	@Column(name = "CREATE_TIME")
	private Date createTime ;

}
