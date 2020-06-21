package com.flowable.oa.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description : 待办类型
 * @since : 2020-06-21 20:29
 */
@Getter
@AllArgsConstructor
public enum HandleTypeEnum {

    ASSIGNEE(1, "待办人"),

    GROUP(2, "角色待办");

    private int type;

    private String desc;

    private static final Map<Integer, HandleTypeEnum> TYPE_MAP = new HashMap<>();

    static {
        for (HandleTypeEnum value : HandleTypeEnum.values()) {
            TYPE_MAP.put(value.getType(), value);
        }
    }

    public static HandleTypeEnum getByType(Integer type) {
        return TYPE_MAP.get(type);
    }
}
