package com.aimsphm.nuclear.common.response;

import com.aimsphm.nuclear.common.exception.BizI18NTransactionException;
import com.aimsphm.nuclear.common.message.I18NHelper;

/**
 * 返回对象封装
 */
public class ResponseUtils {

    public static ResponseData<Object> success() {
        NormalResponse<Object> response = new NormalResponse<Object>();
        response.setResData(null);
        return response;
    }

    public static <T> ResponseData<T> success(T t) {
        NormalResponse<T> response = new NormalResponse<T>();
        response.setResData(t);
        return response;
    }

    public static ResponseData systemError() {
        BizErrorResponse rsp = new BizErrorResponse(RestResponseCode.STSTEM_EXCEPTION);
        return rsp;
    }

    public static ResponseData hystrixError() {
        BizErrorResponse rsp = new BizErrorResponse(RestResponseCode.HYSTRIX_EXCEPTION);
        return rsp;
    }

    public static ResponseData systemError(String msg) {
        BizErrorResponse rsp = new BizErrorResponse(RestResponseCode.STSTEM_EXCEPTION);
        rsp.setErrMsg(msg);
        return rsp;
    }

    public static ResponseData error(BizI18NTransactionException bizEx) {
        BizErrorResponse rsp = new BizErrorResponse(bizEx.getCode());
        rsp.setErrMsg(I18NHelper.getI18NErrorMsg(bizEx.getCode(), bizEx.getPlaceholders()));
        return rsp;
    }

    public static ResponseData error(String code, String msg) {
        return error(code, msg, null);
    }

    public static ResponseData error(String msg) {
        return error(RestResponseCode.STSTEM_EXCEPTION.getCode(), msg, null);
    }

    public static <T> ResponseData<T> errorData(T t) {
        return error(RestResponseCode.STSTEM_EXCEPTION.getCode(), RestResponseCode.STSTEM_EXCEPTION.getLabel(), t);
    }

    public static <T> ResponseData<T> error(String code, String msg, T t) {
        BizErrorResponse<T> rsp = new BizErrorResponse<T>();
        rsp.setResCode(code);
        rsp.setErrMsg(msg);
        rsp.setResData(t);
        return rsp;
    }
}
