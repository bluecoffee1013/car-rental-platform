package com.yepit.mapp.framework.exception;

import com.yepit.mapp.framework.dto.common.BaseResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public BaseResponse serviceExceptionHandler(HttpServletRequest request, ServiceException exception) throws Exception {
        return this.handleErrorInfo(exception.getErrorCode(), exception.getMessage(), exception);
    }

    @ExceptionHandler(CacheException.class)
    @ResponseBody
    public BaseResponse cacheExceptionHandler(HttpServletRequest request, CacheException exception) throws Exception {
        return this.handleErrorInfo(exception.getErrorCode(), exception.getMessage(), exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse exceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        return this.handleErrorInfo("999999", exception.getMessage(), exception);
    }

    private BaseResponse handleErrorInfo(String errorCode, String message, Exception ex) {
        ex.printStackTrace();
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(false);
        resp.setResultCode(errorCode);
        resp.setResultDesc(message);
        return resp;
    }
}
