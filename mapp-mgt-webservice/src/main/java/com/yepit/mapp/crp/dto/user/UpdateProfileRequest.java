package com.yepit.mapp.crp.dto.user;

import com.yepit.mapp.framework.dto.common.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qianlong
 */
@Data
public class UpdateProfileRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 5048741226230078396L;

    private String realname;

    private String mobile;

    private String email;

    private String officePhone;

    private String headImg;

}
