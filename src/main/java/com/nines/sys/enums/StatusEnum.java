package com.nines.sys.enums;

import lombok.Data;

/**
 * 状态
 * @author TYJ
 * @date 2020/10/28 8:36
 */
public enum StatusEnum {

    /**
     * 正常状态
     */
    NORMAL(0, "正常"),
    /**
     * 冻结状态
     */
    freeze(1, "冻结");

    private final int code;

    private final String description;

    StatusEnum(int value, String description) {
        this.code = value;
        this.description = description;
    }

    public int getValue() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
