package com.flowable.oa.entity.dict;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
* @ClassName: DictValue 
* @Description: 枚举值
* @author lukew
* @email 13507615840@163.com
* @date 2017年12月5日 下午9:25:04
 */
@Entity
@Table(name = "T_DICT_VALUE")
public class DictValue implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;
	
	/**
	 * 字典分类id
	 */
	@Column(name = "DICT_TYPE_ID")
	private String dictTypeId;
	
	/**
	 * 字典分类名称
	 */
	@Transient
	private DictType dictType;

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

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;
	
	/**
	 * 编码
	 */
	@Column(name = "CODE")
	private String code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDictTypeId() {
		return dictTypeId;
	}

	public void setDictTypeId(String dictTypeId) {
		this.dictTypeId = dictTypeId;
	}

	public DictType getDictType() {
		return dictType;
	}

	public void setDictType(DictType dictType) {
		this.dictType = dictType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}
