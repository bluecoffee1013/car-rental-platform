package com.yepit.flow.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 任务基本信息
 * @author qianlong
 */
@Data
public class TaskInfoDTO implements Serializable {

    private static final long serialVersionUID = -1421834120741389231L;

    private Long busId;

    private String taskName;

    private Long createUserId;

    private String createUserName;

    private String customerName;

    private String customerMobile;

    private Date createDate;

    private String flowId;

    private Date endDate;

    private String status;
}
