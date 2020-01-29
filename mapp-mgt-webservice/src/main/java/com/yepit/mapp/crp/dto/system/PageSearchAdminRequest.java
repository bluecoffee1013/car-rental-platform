package com.yepit.mapp.crp.dto.system;

import com.yepit.mapp.framework.dto.common.BaseRequest;
import lombok.Data;

/**
 * Created by qianlong on 2017/8/20.
 */
@Data
public class PageSearchAdminRequest extends BaseRequest {

    private static final long serialVersionUID = 1024444725739471780L;

    private Long adminId;

    private String loginName;

    private String realname;

    private String mobile;

    private String email;

    private Integer status;

    private Long roleId;

    private String keyword;
}
