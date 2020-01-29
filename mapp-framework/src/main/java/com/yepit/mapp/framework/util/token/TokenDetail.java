package com.yepit.mapp.framework.util.token;

/**
 * Created by qianlong on 2017/10/15.
 */
public interface TokenDetail {

    //TODO: 这里封装了一层，不直接使用 username 做参数的原因是可以方便未来增加其他要封装到 token 中的信息
    String getUsername();

}
