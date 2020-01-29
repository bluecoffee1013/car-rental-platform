package com.yepit.mapp.framework.util.token;

/**
 * Created by qianlong on 2017/10/15.
 */
public class TokenDetailImpl implements TokenDetail {

    private final String username;

    public TokenDetailImpl(String username)
    {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
