package com.aimsphm.nuclear.common.exception;

/**
 * @Package: com.aimsphm.nuclear.hbase.config
 * @Description: <自定义异常类>
 * @Author: MILLA
 * @CreateDate: 2019/8/15 18:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2019/8/15 18:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class CustomMessageException extends RuntimeException {

    public CustomMessageException() {
        super();
    }

    public CustomMessageException(String message) {
        super(message);
    }

    public CustomMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomMessageException(Throwable cause) {
        super(cause);
    }

    protected CustomMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
