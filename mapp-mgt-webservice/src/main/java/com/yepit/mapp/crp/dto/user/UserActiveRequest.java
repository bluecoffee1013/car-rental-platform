package com.yepit.mapp.crp.dto.user;

import com.yepit.mapp.framework.dto.common.BaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class UserActiveRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 2444057359658367197L;

    private Long userId;

    private List<Long> flowNodeId;

}
