package com.yepit.mapp.crp.mongo.entity;


import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.Date;
import java.util.List;

/**
 * 审核表单数据
 * @author qianlong
 */
@Entity("check_form")
@Indexes(@Index("-createTime") )
public class CheckForm {

    @Id
    private String trackOrderId;

    @Indexed(value=IndexDirection.DESC, name="createTime")
    private Date createTime;

    private String operatorId;

    private String operatorName;

    @Indexed(value=IndexDirection.DESC, name="operateTime")
    private Date operateTime;

    @Indexed(name="operateTime")
    private Integer status;

    private Integer result;

    private String resultDesc;

    private List<String> idCardImgs;

    public String getTrackOrderId() {
        return trackOrderId;
    }

    public void setTrackOrderId(String trackOrderId) {
        this.trackOrderId = trackOrderId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public List<String> getIdCardImgs() {
        return idCardImgs;
    }

    public void setIdCardImgs(List<String> idCardImgs) {
        this.idCardImgs = idCardImgs;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
