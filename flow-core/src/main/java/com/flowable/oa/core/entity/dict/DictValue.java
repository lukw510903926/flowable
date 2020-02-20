package com.flowable.oa.core.entity.dict;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

/**
 * @author lukew
 * @ClassName: DictValue
 * @Description: 枚举值
 * @email 13507615840@163.com
 * @date 2017年12月5日 下午9:25:04
 */
@Data
@Entity
@Table(name = "t_dict_value")
public class DictValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Integer id;

    /**
     * 字典分类id
     */
    @Column(name = "dict_type_id")
    private Integer dictTypeId;

    /**
     * 字典分类名称
     */
    @Transient
    private DictType dictType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "creator")
    private String creator;

    /**
     * 修改时间
     */
    @Column(name = "modified")
    private Date modified;

    /**
     * 修改人
     */
    @Column(name = "modifier")
    private String modifier;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 编码
     */
    @Column(name = "code")
    private String code;
}
