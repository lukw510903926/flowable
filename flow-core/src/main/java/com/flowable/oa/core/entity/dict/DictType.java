package com.flowable.oa.core.entity.dict;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author lukew
 * @ClassName: DictType
 * @Description: 数据枚举字典
 * @email 13507615840@163.com
 * @date 2017年12月5日 下午9:21:58
 */
@Data
@Entity
@Table(name = "t_dict_type")
public class DictType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    /**
     * 字典名称
     */
    @Column(name = "name")
    private String name;

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
}
