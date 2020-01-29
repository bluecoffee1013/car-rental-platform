package com.yepit.mapp.framework.dto.common;

import lombok.Data;

@Data
public class SimpleUserInfo {

    private String userId;

    private String loginName;

    private String realName;

    private Long roleId;
}
