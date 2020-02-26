package com.flowable.oa.core.entity;

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
 * 附件表
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午2:31:19
 * @description :
 */
@Data
@Entity
@Table(name = "t_biz_file")
public class BizFile implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    @Column(name = "biz_id")
    private Long bizId;

    @Column(length = 512, name = "task_name")
    private String taskName;

    @Column(length = 64, name = "task_id")
    private String taskId;

    @Column(length = 256, name = "name")
    private String name;

    @Column(length = 256, name = "create_user")
    private String createUser;

    @Column(name = "create_time")
    private Date createDate;

    @Column(length = 512, name = "path")
    private String path;

    /**
     * 附件类型，FILE,IMAGE (标记为文件或图标)
     */
    @Column(length = 64, name = "filetype")
    private String fileType;

    /**
     * 附件分类
     */
    @Column(length = 64, name = "filecatalog")
    private String fileCatalog;

    @Override
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