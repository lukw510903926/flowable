package com.flowable.oa.core.entity.auth;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午5:14
 **/
@Data
@Entity
@Table(name = "T_SYS_RESOURCE")
public class SystemResource implements Serializable {

    private static final long serialVersionUID = 3181229870633868707L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, length = 64, name = "ID")
    private String id;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 资源名称
     */
    @Column(name = "NAME", length = 64)
    private String name;

    /**
     * 资源路径
     */
    @Column(name = "RESOURCE_URL")
    private String resourceUrl;

    /**
     * 资源标识
     */
    @Column(name = "PERMISSION")
    private String permission;

    /**
     * 父节点
     */
    @Column(name = "PARENT_ID")
    private String parentId;

    @Transient
    private String parentName;

    /**
     * 资源类型 1 模块 2 菜单 3 按钮
     */
    @Column(name = "TYPE")
    private Integer type;
}
