package com.flowable.oa.core.vo;

import com.flowable.oa.core.entity.BizFile;
import com.flowable.oa.core.entity.BizLog;
import com.flowable.oa.core.entity.ProcessVariableInstance;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/23 11:53
 **/
@Data
@ToString
@Accessors(chain = true)
public class BizLogVo implements Serializable {

    private String id;

    private String bizId;

    private String taskName;

    private String taskID;

    private Date createTime;

    private String handleUser;

    private String handleUserName;

    private String userPhone;

    private String userDept;

    private String handleDescription;

    private String handleResult;

    private String handleName;

    private BizLog bizLog;

    private List<ProcessVariableInstance> variableInstances;

    private List<BizFile> bizFiles;
}
