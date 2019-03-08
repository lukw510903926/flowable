package com.flowable.oa.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 附件表
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:31:19
 * @description :
 */
@Data
@Entity
@Table(name = "T_BIZ_FILE")
public class BizFile implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, length = 64, name = "ID")
    private String id;

    @Column(name = "BIZ_ID")
    private String bizId;

    @Column(length = 512, name = "TASK_NAME")
    private String taskName;

    @Column(length = 64, name = "TASK_ID")
    private String taskId;

    @Column(length = 256, name = "NAME")
    private String name;

    @Column(length = 256, name = "CREATE_USER")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createDate;

    @Column(length = 512, name = "PATH")
    private String path;

    /**
     * 附件类型，FILE,IMAGE (标记为文件或图标)
     */
    @Column(length = 64, name = "FILETYPE")
    private String fileType;

    /**
     * 附件分类
     */
    @Column(length = 64, name = "FILECATALOG")
    private String fileCatalog;

    public BizFile clone() {
        BizFile bizFile = null;
        try {
            bizFile = (BizFile) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bizFile;
    }
}