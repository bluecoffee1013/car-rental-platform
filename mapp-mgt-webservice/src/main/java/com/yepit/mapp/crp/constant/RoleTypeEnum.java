package com.yepit.mapp.crp.constant;

import com.yepit.mapp.framework.constant.EnumValue;

/**
 * Created by qianlong on 2018/8/2.
 */
public enum RoleTypeEnum implements EnumValue<Integer,String> {

    SuperAdmin(1,"系统管理员"),MR(2,"医药代表"),StoreMan(3,"药店店员"),
    DataEntryMan(4,"数据录入人员"),Manager(5,"公司管理人员");

    private int value;
    private String desc;

    private RoleTypeEnum(int value, String desc){
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
        for(RoleTypeEnum enumObj: RoleTypeEnum.values()){
            if(enumObj.getValue() == value){
                return enumObj.getDesc();
            }
        }
        return "";
    }
}
