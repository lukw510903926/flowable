package com.flowable.core.bean.dict;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * 
* @ClassName: DictType 
* @Description: 数据枚举字典
* @author lukew
* @email 13507615840@163.com
* @date 2017年12月5日 下午9:21:58
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "T_DICT_TYPE")
public class DictType implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	/**
	 * 字典名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 修改时间
	 */
	@Column(name = "modified")
	private Date modified;

	/**
	 * 修改人
	 */
	@Column(name = "MODIFIER")
	private String modifier;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}
