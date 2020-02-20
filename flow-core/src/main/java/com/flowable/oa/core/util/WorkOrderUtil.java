package com.flowable.oa.core.util;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午3:52:10
 * @description :
 */
@Slf4j
public class WorkOrderUtil {
    /**
     * 根据类型转换
     *
     * @param value
     * @param type
     * @return
     */
    public static Object convObject(String value, String type) {

        if (value == null) {
            return null;
        }
        if (PageComponent.NUMBER.toString().equalsIgnoreCase(type)) {
            if (!value.contains(".")) {
                return Integer.parseInt(value);
            } else {
                return Double.parseDouble(value);
            }
        } else if (PageComponent.DATE.type().equalsIgnoreCase(type)
                || PageComponent.TIME.type().equalsIgnoreCase(type)
                || PageComponent.DATETIME.type().equalsIgnoreCase(type)) {
            return DateUtils.parseDate(value);
        } else if (PageComponent.BOOLEAN.type().equalsIgnoreCase(type)) {
            return "true".equalsIgnoreCase(value) || "1".equals(value);
        }
        return value;
    }

    /**
     * 生成工单号<br>
     * 根据工单类型转换成拼音首字母-yyMMdd-xxxxx
     *
     * @return
     */
    public static String buildWorkNumber(String workType) {
        log.info("workType : {}", workType);
        String workNumber = "OTHER-";
        workNumber = workNumber + DateUtils.formatDate(new Date(), "yyMMdd");
        workNumber = workNumber + "-" + Math.round(Math.random() * 89999 + 10000);
        return workNumber;
    }
}
