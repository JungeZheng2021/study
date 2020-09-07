package com.study.auth.comm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * @Package: com.study.auth.comm
 * @Description: <返回数据>
 * @Author: MILLA
 * @CreateDate: 2018/4/8 9:10
 * @UpdateUser: MILLA
 * @UpdateDate: 2018/4/8 9:10
 * @Version: 1.0
 */
public final class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 7824278330465676943L;

    private static final String SUCCESS_CODE = "1000";

    private static final String SUCCESS_MSG = "success";
    /**
     * 响应编码
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue}, ordinal = 1)
    private String code;

    /**
     * 响应提示
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue}, ordinal = 2)
    private String msg;

    /**
     * 返回的数据
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue}, ordinal = 10)
    private T data;

    public static ResponseData success() {
        return initData(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static ResponseData error(String code) {
        String msg = PropertiesReaderUtil.getProperty(code, null);
        return initData(code, msg, null);
    }

    public static ResponseData error(String code, String msg) {
        return initData(code, msg, null);
    }

    public static <T> ResponseData success(T t) {
        return initData(SUCCESS_CODE, SUCCESS_MSG, t);
    }

    public static <T> ResponseData errorData(String code, T data) {
        String msg = PropertiesReaderUtil.getProperty(code, null);
        return initData(code, msg, data);
    }

    public static <T> ResponseData errorData(String code, String msg, T data) {
        return initData(code, msg, data);
    }

    private static <T> ResponseData initData(String code, String msg, T t) {
        ResponseData data = new ResponseData(SUCCESS_CODE);
        if (!isBlank(msg)) {
            data.setMsg(msg);
        }
        if (!isBlank(code)) {
            data.setCode(code);
        }
        if (t != null) {
            data.setData(t);
        }
        return data;
    }

    private static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public ResponseData() {
    }

    public ResponseData(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}