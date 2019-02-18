package com.flowable.oa.vo;

import com.flowable.oa.entity.BizInfo;
import com.flowable.oa.entity.ProcessVariable;
import com.flowable.oa.entity.auth.SystemUser;
import com.flowable.oa.util.LoginUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p> 工单详情
 * @email 13507615840@163.com
 * @since 19-2-16 下午11:11
 **/
@Data
public class BizInfoVo implements Serializable {

    private static final long serialVersionUID = 7932441802711762L;

    /**
     * 工单基本参数
     */
    private BizInfo bizInfo;

    /**
     * 当前任务名称
     */
    private String currentTaskName;

    /**
     * 创建人
     */
    private SystemUser createUser;

    /**
     * 操作日志
     */
    private List<Map<String,Object>> logs;

    /**
     * 按钮
     */
    private Map<String,String> buttons;

    /**
     * 当前人员
     */
    private LoginUser currentUser;

    /**
     * 当前任务参数
     */
    private List<ProcessVariable> currentVariables;
}
