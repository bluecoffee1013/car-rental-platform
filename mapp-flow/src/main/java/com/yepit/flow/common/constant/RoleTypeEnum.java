package com.yepit.flow.common.constant;

import com.yepit.mapp.framework.constant.EnumValue;

/**
 * 角色类型定义
 * Created by qianlong on 2019/1/20.
 */
public enum RoleTypeEnum implements EnumValue<Integer, String> {

    Partner(6, "合作方");

    private int value;
    private String desc;

    private RoleTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static String getDescByValue(Integer value) {
        for (RoleTypeEnum enumObj : RoleTypeEnum.values()) {
            if (enumObj.getValue() == value) {
                return enumObj.getDesc();
            }
        }
        return "";
    }

}
