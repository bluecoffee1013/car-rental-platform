package com.yepit.mapp.framework.dto.common;

public class HttpResult {

    private Integer statusCode;

    private String readObject;

    public HttpResult(Integer statusCode,String readObject){
        this.statusCode = statusCode;
        this.readObject = readObject;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getReadObject() {
        return readObject;
    }

    public void setReadObject(String readObject) {
        this.readObject = readObject;
    }

}
