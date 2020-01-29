package com.yepit.flow.mongo.entity;

import com.yepit.flow.dto.DocInfo;
import lombok.Data;

import java.util.List;

@Data
public class FlowTaskDetailInfo {

    /**
     * 上传的文件
     */
    private List<DocInfo> docList;

    /**
     * 押金
     */
    private Long deposit;

    /**
     * 预约时间
     */
    private String appointmentTime;

    /**
     * 预约地点
     */
    private String appointmentPlace;

    /**
     * 家访时间
     */
    private String homeVisitTime;

    /**
     * 家访地点
     */
    private String homeVisitPlace;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户手机号
     */
    private String customerMobile;

    /**
     * 保险是否已出单
     */
    private String isInsurance;
}
