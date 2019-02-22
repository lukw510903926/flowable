package com.flowable.oa.core.util;

public class Constants {

    /**
     * 待办组权限前缀标识
     */
    public final static String BIZ_GROUP = "GROUP:";

    /**
     * 会签人员参数标识
     */
    public final static String COUNTER_SIGN = "_members";

    /**
     * 开始节点
     */
    public final static String TASK_START = "START";

    /**
     * 按钮
     */
    public final static String SYS_BUTTON_VALUE = "SYS_BUTTON_VALUE";

    /**
     * 签收标识
     */
    public final static String SIGN = "SIGN";

    /**
     * 处理标识
     */
    public final static String HANDLE = "HANDLE";


    //============== 流程参数相关 =============
    /**
     * 工单创建人
     */
    public final static String SYS_BIZ_CREATEUSER = "SYS_BIZ_CREATEUSER";

    /**
     * 工单ID
     */
    public final static String SYS_BIZ_ID = "SYS_BIZ_ID";

    //=====================工单状态===========================

    /**
     * 草稿
     */
    public final static String BIZ_TEMP = "草稿";

    /**
     * 新建
     */
    public final static String BIZ_NEW = "新建";

    /**
     * 结束
     */
    public final static String BIZ_END = "已完成";

    /**
     * 删除
     */
    public final static String BIZ_DELETE = "已删除";

    //=====================会签结果类型===========================

    /**
     * 通过
     */
    public final static int MEET_YES = 1;

    /**
     * 驳回
     */
    public final static int MEET_NO = 0;
}
