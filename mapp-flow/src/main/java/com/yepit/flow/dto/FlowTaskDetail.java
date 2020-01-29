package com.yepit.flow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 任务详情
 */
@ApiModel
@Data
public class FlowTaskDetail implements Serializable {

    private static final long serialVersionUID = 3377331975854246210L;

    /**
     * 上传的文件
     */
    @ApiModelProperty(value = "上传的文件", name = "docList")
    private List<DocInfo> docList;

    /**
     * 押金
     */
    @ApiModelProperty(value = "押金", name = "deposit")
    private Long deposit;

    /**
     * 预约时间
     */
    @ApiModelProperty(value = "预约时间(YYYY-MM-DD hh:mm)", name = "appointmentTime")
    private String appointmentTime;

    /**
     * 预约地点
     */
    @ApiModelProperty(value = "预约地点", name = "appointmentPlace")
    private String appointmentPlace;

    /**
     * 家访时间
     */
    @ApiModelProperty(value = "家访时间(YYYY-MM-DD hh:mm)", name = "homeVisitTime")
    private String homeVisitTime;

    /**
     * 家访地点
     */
    @ApiModelProperty(value = "家访地点", name = "homeVisitPlace")
    private String homeVisitPlace;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户姓名", name = "customerName")
    private String customerName;

    /**
     * 客户手机号
     */
    @ApiModelProperty(value = "客户手机号", name = "customerMobile")
    private String customerMobile;

    /**
     * 保险是否已出单
     */
    @ApiModelProperty(value = "保险是否已出单(1-已出,2-未出)", name = "isInsurance")
    private String isInsurance;
}
