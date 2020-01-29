package com.yepit.mapp.framework.constant;

/**
 * Created by qianlong on 2017/7/4.
 */
public interface EnumValue<K,V> {

    /**
     * @return 返回这个枚举对象的值
     */
    public K getValue();

    /**
     * @return 返回这个值的描述
     */
    public V getDesc();

}
