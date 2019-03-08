package com.flowable.oa.core.entity.dict;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
* @ClassName: DictType 
* @Description: 数据枚举字典
* @author lukew
* @email 13507615840@163.com
* @date 2017年12月5日 下午9:21:58
 */
@Data
@Entity
@Table(name = "T_DICT_TYPE")
public class DictType implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
