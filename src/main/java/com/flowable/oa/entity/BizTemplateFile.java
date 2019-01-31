package com.flowable.oa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 附件模板文件
 * @author 26223
 *
 */
@Entity
@Table(name = "T_BIZ_TEMPLATE_FILE")
public class BizTemplateFile implements Serializable{

	private static final long serialVersionUID = 8831885227643640365L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	private String id;
	
	@Column(name = "CREATE_USER", length = 64)
	private String createUser;
	
	@Column(name = "FILE_NAME", length = 64)
	private String fileName;
	
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	private Timestamp createTime;
	
	@Column(name = "FLOW_NAME", nullable = false, length = 32)
	private String flowName;

	public BizTemplateFile() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
}