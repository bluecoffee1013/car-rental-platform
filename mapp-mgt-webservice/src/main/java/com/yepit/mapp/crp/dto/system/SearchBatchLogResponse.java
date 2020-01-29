package com.yepit.mapp.crp.dto.system;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qianlong on 2017/9/2.
 */
public class SearchBatchLogResponse implements Serializable {

    private static final long serialVersionUID = 1047575934069487525L;

    private String batchId;

    private String fileId;

    private String uploadFilePath;

    private String uploadFileUrl;

    private Integer batchType;

    private String batchTypeName;

    private Integer status;

    private String statusName;

    private String resultDesc;

    private String resultFilePath;

    private String resultFileUrl;

    private Date createTime;

    private String createOperator;

    private Date updateTime;

    private String updateOperator;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public String getUploadFileUrl() {
        return uploadFileUrl;
    }

    public void setUploadFileUrl(String uploadFileUrl) {
        this.uploadFileUrl = uploadFileUrl;
    }

    public Integer getBatchType() {
        return batchType;
    }

    public void setBatchType(Integer batchType) {
        this.batchType = batchType;
    }

    public String getBatchTypeName() {
        return batchTypeName;
    }

    public void setBatchTypeName(String batchTypeName) {
        this.batchTypeName = batchTypeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public String getResultFileUrl() {
        return resultFileUrl;
    }

    public void setResultFileUrl(String resultFileUrl) {
        this.resultFileUrl = resultFileUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator;
    }
}
