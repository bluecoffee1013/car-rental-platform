package com.yepit.mapp.framework.util.idgenerate;

/**
 * Created by qianlong on 2017/8/17.
 */
public class DefaultIdGeneratorConfig implements IdGeneratorConfig{

    @Override
    public String getSplitString() {
        return "";
    }

    @Override
    public int getInitial() {
        return 1;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public int getRollingInterval() {
        return 5;
    }

}