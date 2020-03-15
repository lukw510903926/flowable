package com.flowable.oa.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/9 11:19
 **/
@Data
public class ProcessDefinitionEntityVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -3155734440294240387L;

    /**
     * 流程ID
     */
    private String id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程标识
     */
    private String key;

    /**
     * 流程版本
     */
    private int version;

    private String category;

    private String deploymentId;

    private String resourceName;

    /**
     * 部署时间
     */
    private Date deploymentTime;
}
