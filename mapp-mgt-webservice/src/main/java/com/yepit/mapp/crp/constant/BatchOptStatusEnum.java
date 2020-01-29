package com.yepit.mapp.crp.constant;

import com.yepit.mapp.framework.constant.EnumValue;

/**
 * Created by qianlong on 2017/8/27.
 */
public enum BatchOptStatusEnum implements EnumValue<Integer,String> {

    Pending(1,"待处理"),Processing(2,"处理中"),Successful(3,"处理结束-全部成功"),Fail(4,"处理结束-部分失败");

    private int value;
    private String desc;

    private BatchOptStatusEnum(int value, String desc){
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
        for(BatchOptStatusEnum enumObj: BatchOptStatusEnum.values()){
            if(enumObj.getValue() == value){
                return enumObj.getDesc();
            }
        }
        return "";
    }
}
