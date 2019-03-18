package com.flowable.oa.core.entity;

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
	@Column(unique = true, length = 64, name = "ID")
	private Integer id;
	
	@Column(name="TASK_ID")
	private String taskId;

	@Column(name="BIZ_ID")
	private String bizId;
	
	@Column(name="PROCESSINSTANCE_ID")
	private String processInstanceId;
	
	@Column(name="PROCESSDEFINITION_ID")
	private String processDefinitionId;
	
	@Column(name="TASK_ASSIGNEE")
	private String taskAssignee;
	
	@Column(name="RESULT_TYPE")
	private Integer resultType;

	/**
	 * 当前会签是否结束 0 没有,1 结束
	 */
	@Column(name="IS_COMPLETE")
	private Integer isComplete = 0;
	
	@Column(name = "CREATE_TIME")
	private Date createTime ;

}
