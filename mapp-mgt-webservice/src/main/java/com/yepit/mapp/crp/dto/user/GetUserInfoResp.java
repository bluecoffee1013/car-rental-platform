package com.yepit.mapp.crp.dto.user;

import com.yepit.mapp.crp.dto.system.AdminDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class GetUserInfoResp implements Serializable {

    private static final long serialVersionUID = -3030087612790507879L;

    private AdminDTO userInfo;

    private List<UserPrivilegeDTO> userPrivilegeList;

}
