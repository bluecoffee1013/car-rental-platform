package com.yepit.mapp.framework.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by qianlong on 2017/8/19.
 */
@Data
@ApiModel
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public BaseRequest() {
        super();
        init();
    }

    private void init() {
        // 当没有时，自动生成一个。发生在web新建对象时。当到服务提供者端时，已经有了。
        if (null == traceId || "".equals(traceId))
            traceId = UUID.randomUUID().toString().replaceAll("\\-", "").toUpperCase();
    }

    public BaseRequest(PageArg page) {
        super();
        init();
        this.page = page;
    }

    public BaseRequest(int pageNum, int pageSize) {
        super();
        init();
        this.page = new PageArg(pageNum, pageSize);
    }

    /**
     * traceId，必填
     */
    @ApiModelProperty(value = "traceId", name = "traceId", hidden = true)
    private String traceId;

    @ApiModelProperty(value = "分页查询参数(分页查询时必填)", name = "page", hidden = true)
    private PageArg page;

    /**
     * 工号
     */
    @ApiModelProperty(value = "操作员ID", name = "operId", hidden = true)
    private String operId;

    /**
     * 操作员登录名
     */
    @ApiModelProperty(value = "操作员登录名", name = "operName", hidden = true)
    private String operName;


}
