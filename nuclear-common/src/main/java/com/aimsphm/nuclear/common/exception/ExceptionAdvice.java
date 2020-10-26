package com.aimsphm.nuclear.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aimsphm.nuclear.common.message.I18NHelper;
import com.aimsphm.nuclear.common.response.BizErrorResponse;
import com.aimsphm.nuclear.common.response.RestResponseCode;


@ControllerAdvice
public class ExceptionAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<?> handleMissingParameter(Throwable ex) {
    	ex.printStackTrace();//TODO 上线后可删除
     if(ex instanceof BizI18NTransactionException){
    		BizI18NTransactionException bizEx = (BizI18NTransactionException) ex;
    		BizErrorResponse rsp = new BizErrorResponse(bizEx.getCode());
    		rsp.setErrMsg(I18NHelper.getI18NErrorMsg(bizEx.getCode(),bizEx.getPlaceholders()));
    		return ResponseEntity.ok(rsp);
    	}
		else if (ex instanceof IllegalArgumentException) {
			BizErrorResponse rsp = new BizErrorResponse();
			rsp.setResCode(RestResponseCode.STSTEM_EXCEPTION.getCode());
			rsp.setErrMsg(ex.getMessage());
			return ResponseEntity.ok(rsp);
		}
    	else {
    		LOGGER.error("Unknown error", ex);
    		BizErrorResponse rsp = new BizErrorResponse();
			rsp.setResCode(RestResponseCode.STSTEM_EXCEPTION.getCode());
			rsp.setErrMsg(ex.getMessage());
			return ResponseEntity.ok(rsp);
		
    	}
    }
 
}