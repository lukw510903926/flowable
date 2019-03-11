package com.flowable.oa.core.entity.auth;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 19-2-17 下午7:08
 **/
@Data
@Entity
@Table(name = "T_SYS_ROLE_RESOURCE")
public class SysRoleResource implements Serializable {

    private static final long serialVersionUID = -4427357760034390938L;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, length = 64, name = "ID")
    private Integer id;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "RESOURCE_ID")
    private String resourceId;
}
