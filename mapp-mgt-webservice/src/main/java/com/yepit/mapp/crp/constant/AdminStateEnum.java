package com.yepit.mapp.crp.constant;

import com.yepit.mapp.framework.constant.EnumValue;

/**
 * Created by qianlong on 2017/8/19.
 */
public enum AdminStateEnum implements EnumValue<Integer,String> {

    Normal(1,"正常"),Cancel(2,"注销"),WaitingForApprove(3,"待审核");

    private int value;
    private String desc;

    private AdminStateEnum(int value, String desc){
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
        for(AdminStateEnum adminStateEnum:AdminStateEnum.values()){
            if(adminStateEnum.getValue() == value){
                return adminStateEnum.getDesc();
            }
        }
        return "";
    }

}
