package com.flowable.entity;

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
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 附件表
 */
@Entity
@Table(name = "T_BIZ_FILE")
@DynamicInsert
@DynamicUpdate
public class BizFile implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIZ_ID")
	private BizInfo bizInfo;
	
	@Transient
	private String bizId;

	@Column(length = 512, name = "TASK_NAME")
	private String taskName;
	
	@Column(length=64,name = "TASK_ID")
	private String taskId;

	@Column(nullable = false, length = 256, name = "NAME")
	private String name;

	@Column(nullable = false, length = 256, name = "CREATE_USER")
	private String createUser;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createDate;

	@Column(nullable = false, length = 512, name = "PATH")
	private String path;

	/**
	 * 附件类型，FILE,IMAGE (标记为文件或图标)
	 */
	@Column(nullable = false, length = 64, name = "FILETYPE")
	private String fileType;

	/**
	 * 附件分类
	 */
	@Column(nullable = false, length = 64, name = "FILECATALOG")
	private String fileCatalog;
	
	@Column(length = 512, name = "DESCRIPTION")
	private String description;

	public BizFile() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BizInfo getBizInfo() {
		return bizInfo;
	}

	public void setBizInfo(BizInfo bizInfo) {
		this.bizInfo = bizInfo;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public BizFile clone() {
		BizFile bizFile = null;
		try {
			bizFile = (BizFile) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bizFile;
	}

	public String getFileCatalog() {
		return fileCatalog;
	}

	public void setFileCatalog(String fileCatalog) {
		this.fileCatalog = fileCatalog;
	}

}