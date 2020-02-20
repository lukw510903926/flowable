package com.flowable.oa.core.entity.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "t_sys_role")
public class SystemRole implements Serializable {

    private static final long serialVersionUID = -8144101409950412071L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "name_en", length = 64)
    private String nameEn;

    @Column(name = "name_cn", length = 64)
    private String nameCn;

    @Transient
    private Set<SystemUser> users = new HashSet<>();
}
