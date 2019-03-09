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
public class ProcessDefinitionEntityVo implements Serializable {

    private static final long serialVersionUID = -3155734440294240387L;
    
    private String name;

    private String description;

    private String key;

    private int version;

    private String category;

    private String deploymentId;

    private String resourceName;

    private Date deploymentTime;
}
