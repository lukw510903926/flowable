package com.flowable.oa.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 附件模板文件
 * @author 26223
 *
 */
@Data
@Entity
@Table(name = "T_BIZ_TEMPLATE_FILE")
public class BizTemplateFile implements Serializable{

	private static final long serialVersionUID = 8831885227643640365L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	
	@Column(name = "CREATE_USER", length = 64)
	private String createUser;
	
	@Column(name = "FILE_NAME", length = 64)
	private String fileName;
	
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	private Timestamp createTime;
	
	@Column(name = "FLOW_NAME", nullable = false, length = 32)
	private String flowName;

	/**
	 * 创建人姓名
	 */
	@Column(name = "FULL_NAME")
	private String fullName;
}