package com.yepit.mapp.crp.constant;

import com.yepit.mapp.framework.constant.EnumValue;

/**
 * Created by qianlong on 2017/8/19.
 */
public enum OTPTypeEnum implements EnumValue<Integer,String> {

    ModifyPwd(1,"ModifyPassword");

    private int value;
    private String desc;

    private OTPTypeEnum(int value, String desc){
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
        for(OTPTypeEnum objEnum:OTPTypeEnum.values()){
            if(objEnum.getValue() == value){
                return objEnum.getDesc();
            }
        }
        return "";
    }

}
