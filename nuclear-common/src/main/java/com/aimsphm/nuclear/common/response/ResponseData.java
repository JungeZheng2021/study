package com.aimsphm.nuclear.common.response;


import lombok.Data;

import java.io.Serializable;

/**
 * @Package: com.aimsphm.nuclear.common.response
 * @Description: <返回数据>
 * @Author: MILLA
 * @CreateDate: 2018/4/8 9:10
 * @UpdateUser: MILLA
 * @UpdateDate: 2018/4/8 9:10
 * @Version: 1.0
 */
@Data
public final class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 7824278330465676943L;

    public static final String SUCCESS_CODE = "1000";

    private static final String SUCCESS_MSG = "success";
    /**
     * 响应编码
     */
    private String code;

    /**
     * 响应提示
     */
    private String msg;

    /**
     * 返回的数据
     */
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
        ResponseData data = new ResponseData();
        data.setCode(SUCCESS_CODE);
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
        }
        return true;
    }

    public ResponseData() {
    }
}