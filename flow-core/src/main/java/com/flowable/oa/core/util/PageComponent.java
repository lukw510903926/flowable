package com.flowable.oa.core.util;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description :
 * @since : 2020-02-20 17:41
 */
public enum PageComponent {

    TEXT("文本"),

    TEXTAREA("文本域"),

    NUMBER("数字"),

    DATE("日期"),

    TIME("时间"),

    DATETIME("日期时间"),

    BOOLEAN("布尔"),

    MOBILE("手机号"),

    EMAIL("邮箱");

    private String name;

    PageComponent(String name) {
        this.name = name;
    }

    public String type() {
        return this.name;
    }
}
