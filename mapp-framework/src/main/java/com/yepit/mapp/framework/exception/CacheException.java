package com.yepit.mapp.framework.exception;

/**
 * @author qianlong
 * 缓存层异常
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 构造一个基本异常
     *
     * @param message
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * 构造一个基本异常
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     * @param cause 根异常类（可以存入任何异常）
     */
    public CacheException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.setErrorCode(errorCode);
    }

    /**
     * 构造一个基本异常
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public CacheException(String errorCode, String message) {
        super(message);
        this.setErrorCode(errorCode);
    }

    /**
     * 构造一个基本异常.
     *
     * @param message   信息描述
     * @param cause 根异常类（可以存入任何异常）
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
