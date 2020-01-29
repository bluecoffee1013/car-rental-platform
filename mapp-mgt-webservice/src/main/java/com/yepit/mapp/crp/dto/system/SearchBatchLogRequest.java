package com.yepit.mapp.crp.dto.system;

import com.yepit.mapp.framework.dto.common.BaseRequest;

/**
 * Created by qianlong on 2017/9/2.
 */
public class SearchBatchLogRequest extends BaseRequest {

    private static final long serialVersionUID = -5647450503090872711L;

    private Integer status;

    private Integer batchType;

    private String startTime;

    private String endTime;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBatchType() {
        return batchType;
    }

    public void setBatchType(Integer batchType) {
        this.batchType = batchType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
