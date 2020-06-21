package com.flowable.oa.core.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :附件模板文件
 * @since : 2020/6/21 20:47
 */
@Data
@Entity
@Table(name = "t_biz_template_file")
public class BizTemplateFile implements Serializable {

    private static final long serialVersionUID = 8831885227643640365L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "create_user", length = 64)
    private String createUser;

    @Column(name = "file_name", length = 64)
    private String fileName;

    @Column(name = "create_time", nullable = false, length = 19)
    private Date createTime;

    @Column(name = "flow_name", nullable = false, length = 32)
    private String flowName;

    /**
     * 创建人姓名
     */
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "file_path")
    private String filePath;
}