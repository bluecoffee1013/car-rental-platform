package com.yepit.mapp.framework.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yepit.mapp.framework.constant.ResultCodeConstant;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by qianlong on 2017/8/19.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isSuccess;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resultCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resultDesc;

    /**
     * 业务数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result = null;

    /**
     * 用于异常时，存放堆栈信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object stackInfo = "";

    /**
     * 默认构造函数
     */
    public BaseResponse() {
        super();
    }

    /**
     * 构造函数
     *
     * @param success       是否成功
     * @param resultCode    是否成功代码
     * @param resultMessage 是否成功信息
     */
    public BaseResponse(boolean success, String resultCode, String resultMessage) {
        this.isSuccess = success;
        this.resultCode = resultCode;
        this.resultDesc = resultMessage;
    }

    /**
     * 构造函数
     *
     * @param success       是否成功
     * @param resultCode    是否成功代码
     * @param resultMessage 是否成功信息
     * @param stackInfo     扩展信息 ，异常时存异常储堆栈信息
     */
    public BaseResponse(boolean success, String resultCode, String resultMessage, Object stackInfo) {
        super();
        this.isSuccess = success;
        this.resultCode = resultCode;
        this.resultDesc = resultMessage;
        this.stackInfo = stackInfo;
    }

    /**
     * 构造函数
     *
     * @param success       是否成功
     * @param resultCode    是否成功代码
     * @param resultMessage 是否成功信息
     * @param stackInfo     扩展信息 ，异常时存异常储堆栈信息
     * @param result        业务执行结果
     */
    public BaseResponse(boolean success, String resultCode, String resultMessage, Object stackInfo, T result) {
        super();
        this.isSuccess = success;
        this.resultCode = resultCode;
        this.resultDesc = resultMessage;
        this.stackInfo = stackInfo;
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public BaseResponse<T> setSuccessfulResp(String msg, Object data) {
        return new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, msg, null, data);
    }

    public BaseResponse<T> setSuccessfulResp(String msg) {
        return new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, msg, null, null);
    }

    public BaseResponse<T> successful(String msg) {
        return new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, msg, null, null);
    }

    public BaseResponse<T> setFailResp(String errCode, String msg) {
        return new BaseResponse(false, errCode, msg, null);
    }

    public BaseResponse<T> setFailResp(String errCode, String msg, String stackInfo) {
        return new BaseResponse(false, errCode, msg, stackInfo);
    }

    public BaseResponse<T> fail(String errCode, String msg) {
        return new BaseResponse(false, errCode, msg, stackInfo);
    }

    public BaseResponse<T> fail(String msg) {
        return new BaseResponse(false, ResultCodeConstant.UNKNOWERR, msg, stackInfo);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.JSON_STYLE);
    }
}
