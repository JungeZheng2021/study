package com.aimsphm.nuclear.common.response;

import com.aimsphm.nuclear.common.exception.BizI18NTransactionException;
import com.aimsphm.nuclear.common.message.I18NHelper;

import org.springframework.http.ResponseEntity;

/**
 * 返回对象封装
 */
public class ResponseUtils {

	public static ReturnResponse<Object> success() {
		NormalResponse<Object> response = new NormalResponse<Object>();
		response.setResData(null);
		return response;
	}

	public static <T> ReturnResponse<T> success(T t) {
		NormalResponse<T> response = new NormalResponse<T>();
		response.setResData(t);
		return response;
	}

	public static ReturnResponse systemError() {
		BizErrorResponse rsp = new BizErrorResponse(RestResponseCode.STSTEM_EXCEPTION);
		return rsp;
	}

	public static ReturnResponse hystrixError() {
		BizErrorResponse rsp = new BizErrorResponse(RestResponseCode.HYSTRIX_EXCEPTION);
		return rsp;
	}
	
	public static ReturnResponse systemError(String msg) {
		BizErrorResponse rsp = new BizErrorResponse(RestResponseCode.STSTEM_EXCEPTION);
		rsp.setErrMsg(msg);
		return rsp;
	}

	public static ReturnResponse error(BizI18NTransactionException bizEx) {
		BizErrorResponse rsp = new BizErrorResponse(bizEx.getCode());
		rsp.setErrMsg(I18NHelper.getI18NErrorMsg(bizEx.getCode(), bizEx.getPlaceholders()));
		return rsp;
	}

	public static ReturnResponse error(String code, String msg) {
		return error(code, msg, null);
	}

	public static ReturnResponse error(String msg) {
		return error(RestResponseCode.STSTEM_EXCEPTION.getCode(), msg, null);
	}

	public static <T> ReturnResponse<T> errorData(T t) {
		return error(RestResponseCode.STSTEM_EXCEPTION.getCode(), RestResponseCode.STSTEM_EXCEPTION.getLabel(), t);
	}

	public static <T> ReturnResponse<T> error(String code, String msg, T t) {
		BizErrorResponse<T> rsp = new BizErrorResponse<T>();
		rsp.setResCode(code);
		rsp.setErrMsg(msg);
		rsp.setResData(t);
		return rsp;
	}
}
