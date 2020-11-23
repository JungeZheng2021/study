package com.aimsphm.nuclear.common.exception;

import com.aimsphm.nuclear.common.constant.CodeMessageConstant;
import com.aimsphm.nuclear.common.response.PropertiesReaderUtil;
import com.aimsphm.nuclear.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountException;
import java.io.IOException;
import java.sql.SQLException;

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
    private ResponseData responseData(String code, Exception e) {
        log.error("异常代码:{},异常描述:{},异常堆栈:", code, PropertiesReaderUtil.getProperty(code), e);
        return ResponseData.error(code);
    }

    private ResponseData<String> responseData(String code, String message, Exception e) {
        log.error("异常代码:{},异常描述:{},异常堆栈:", code, message, e);
        return ResponseData.error(code, message);
    }

    /**
     * 运行时异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseData runtimeExceptionHandler(Exception e) {
        return responseData(CodeMessageConstant.EX_RUN_TIME_EXCEPTION, e);
    }

    /**
     * 处理SQLSyntaxErrorException
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ResponseData<String> sqlException(SQLException e) {
        return responseData(CodeMessageConstant.EX_RUN_TIME_EXCEPTION, e.getMessage(), e);
    }


    /**
     * 处理CustomerMessageException
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(CustomMessageException.class)
    public ResponseData<String> customerMessageException(CustomMessageException e) {
        return responseData(CodeMessageConstant.EX_RUN_TIME_EXCEPTION, e.getMessage(), e);
    }

    /**
     * 处理AccountException
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(AccountException.class)
    public ResponseData<String> accountException(AccountException e) {
        return responseData(e.getMessage(), e);
    }


    //---------------------------------------jdk/spring自带的异常----------------------------------

    /**
     * 处理IllegalArgumentException
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseData<String> illegalArgumentException(IllegalArgumentException e) {
        return responseData(CodeMessageConstant.EX_RUN_TIME_EXCEPTION, e.getMessage(), e);
    }

    /**
     * 空指针异常
     *
     * @param e 异常
     * @return
     */
    @ResponseStatus
    @ExceptionHandler(NullPointerException.class)
    public ResponseData nullPointerExceptionHandler(NullPointerException e) {
        return responseData(CodeMessageConstant.EX_NULL_POINTER_EXCEPTION, e);
    }

    /**
     * 类型转换异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public ResponseData classCastExceptionHandler(ClassCastException e) {
        return responseData(CodeMessageConstant.EX_CLASS_CAST_EXCEPTION, e);
    }

    /**
     * IO异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(IOException.class)
    public ResponseData iOExceptionHandler(IOException e) {
        return responseData(CodeMessageConstant.EX_IO_EXCEPTION, e);
    }

    /**
     * 未知方法异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseData noSuchMethodExceptionHandler(NoSuchMethodException e) {
        return responseData(CodeMessageConstant.EX_NO_SUCH_METHOD_EXCEPTION, e);
    }

    /**
     * 数组越界异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseData indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException e) {
        return responseData(CodeMessageConstant.EX_INDEX_OUT_OF_BOUNDS_EXCEPTION, e);
    }

    /**
     * 请求body缺失异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseData requestNotReadable(HttpMessageNotReadableException e) {
        return responseData(CodeMessageConstant.EX_HTTP_MESSAGE_NOT_READABLE_EXCEPTION, e);
    }

    /**
     * 类型匹配异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    public ResponseData requestTypeMismatch(TypeMismatchException e) {
        return responseData(CodeMessageConstant.EX_HTTP_MESSAGE_NOT_READABLE_EXCEPTION, e);
    }

    /**
     * 方法不支持异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseData methodNotSupported(HttpRequestMethodNotSupportedException e) {
        return responseData(CodeMessageConstant.EX_HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION, e);
    }

    /**
     * 请求头不支持异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseData mediaTypeNotAcceptable(HttpMediaTypeNotSupportedException e) {
        return responseData(CodeMessageConstant.EX_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_EXCEPTION, e);
    }

//    /**
//     * 参数解析异常
//     *
//     * @param e 异常
//     * @return
//     */
//    @ExceptionHandler(JSONException.class)
//    public ResponseData runtimeExceptionHandler(JSONException e) {
//        return responseData(CodeMessageConstant.PARAMS_PARSE_EXCEPTION, e);
//    }

    /**
     * 参数解析异常
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(JsonParseException.class)
    public ResponseData runtimeExceptionHandler(JsonParseException e) {
        return responseData(CodeMessageConstant.PARAMS_PARSE_EXCEPTION, e);
    }

    /**
     * 请求参数缺失异常
     *
     * @param e 异常
     * @return
     */

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseData requestMissingServletRequest(MissingServletRequestParameterException e) {
        return responseData(CodeMessageConstant.EX_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION, e);
    }

    /**
     * 参数不能为空
     *
     * @param e 异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData exceptionHandler(MethodArgumentNotValidException e) {
        return responseData(CodeMessageConstant.PARAMS_IS_NULL, e);
    }
}
