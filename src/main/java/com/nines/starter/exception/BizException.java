package com.nines.starter.exception;

import lombok.Data;

/**
 * @author TYJ
 * @date 2020/10/21 10:08
 */
@Data
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    protected Integer errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BizException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BizException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
