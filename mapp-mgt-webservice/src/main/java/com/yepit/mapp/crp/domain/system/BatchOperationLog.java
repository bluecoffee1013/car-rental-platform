package com.yepit.mapp.crp.domain.system;

import java.util.Date;

public class BatchOperationLog {
    private String batchId;

    private String fileId;

    private String uploadFilePath;

    private String uploadFileUrl;

    private Integer batchType;

    private Integer status;

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
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath == null ? null : uploadFilePath.trim();
    }

    public String getUploadFileUrl() {
        return uploadFileUrl;
    }

    public void setUploadFileUrl(String uploadFileUrl) {
        this.uploadFileUrl = uploadFileUrl == null ? null : uploadFileUrl.trim();
    }

    public Integer getBatchType() {
        return batchType;
    }

    public void setBatchType(Integer batchType) {
        this.batchType = batchType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc == null ? null : resultDesc.trim();
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath == null ? null : resultFilePath.trim();
    }

    public String getResultFileUrl() {
        return resultFileUrl;
    }

    public void setResultFileUrl(String resultFileUrl) {
        this.resultFileUrl = resultFileUrl == null ? null : resultFileUrl.trim();
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
        this.createOperator = createOperator == null ? null : createOperator.trim();
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
        this.updateOperator = updateOperator == null ? null : updateOperator.trim();
    }
}