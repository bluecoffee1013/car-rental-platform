package com.yepit.flow.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BusMilestoneDTO implements Serializable {

    private static final long serialVersionUID = -232767160359964477L;

    private Long id;

    private Long busId;

    private String taskId;

    private String taskDefId;

    private String currentTask;

    private Long assigementUser;

    private String assigementUserName;

    private String doneResult;

    private String doneRemark;

    private Date startDate;

    private Date endDate;

    private String status;

    private String customerName;

    private String customerMobile;

}