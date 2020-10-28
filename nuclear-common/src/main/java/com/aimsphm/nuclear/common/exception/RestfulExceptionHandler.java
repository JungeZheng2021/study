package com.aimsphm.nuclear.common.exception;

import com.aimsphm.nuclear.common.message.I18NHelper;
import com.aimsphm.nuclear.common.response.BizErrorResponse;
import com.aimsphm.nuclear.common.response.RestResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Package: com.study.auth.exception
 * @Description: <所有异常拦截类>
 * @Author: milla
 * @CreateDate: 2020/09/04 15:35
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/04 15:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class RestfulExceptionHandler {
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<?> handleMissingParameter(Throwable ex) {
        log.error("get a error message ：{}", ex);
        if (ex instanceof BizI18NTransactionException) {
            BizI18NTransactionException bizEx = (BizI18NTransactionException) ex;
            BizErrorResponse rsp = new BizErrorResponse(bizEx.getCode());
            rsp.setErrMsg(I18NHelper.getI18NErrorMsg(bizEx.getCode(), bizEx.getPlaceholders()));
            return ResponseEntity.ok(rsp);
        }
        BizErrorResponse rsp = new BizErrorResponse();
        rsp.setResCode(RestResponseCode.STSTEM_EXCEPTION.getCode());
        rsp.setErrMsg(ex.getMessage());
        return ResponseEntity.ok(rsp);

    }

}

