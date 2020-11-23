package com.aimsphm.nuclear.common.constant;

/**
 * @Package: com.aimsphm.nuclear.common.constant
 * @Description: <公共常量类>
 * @Author: MILLA
 * @CreateDate: 2020/09/04 15:37
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/09/04 15:37
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class CodeMessageConstant {

    /**
     * 用户未登录
     */
    public static final String EX_NO_TOKEN_EXCEPTION = "1001";

    //--------------------------------非业务返回码---------------------------------------
    /**
     * 运行时异常
     */
    public static final String EX_RUN_TIME_EXCEPTION = "1100";
    /**
     * 空指针异常
     */
    public static final String EX_NULL_POINTER_EXCEPTION = "1101";
    /**
     * 数据转换异常
     */
    public static final String EX_CLASS_CAST_EXCEPTION = "1102";
    /**
     * IO异常
     */
    public static final String EX_IO_EXCEPTION = "1103";
    /**
     * 找不到该方法异常
     */
    public static final String EX_NO_SUCH_METHOD_EXCEPTION = "1104";
    /**
     * 数组越界异常
     */
    public static final String EX_INDEX_OUT_OF_BOUNDS_EXCEPTION = "1105";
    /**
     * 请求体缺失异常
     */
    public static final String EX_HTTP_MESSAGE_NOT_READABLE_EXCEPTION = "1106";
    /**
     * TYPE匹配异常
     */
    public static final String EX_TYPE_MISMATCH_EXCEPTION = "1107";
    /**
     * 请求参数丢失
     */
    public static final String EX_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION = "1108";
    /**
     * 请求方法类型不支持异常
     */
    public static final String EX_HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION = "1109";
    /**
     * MEDIA 类型不支持异常
     */
    public static final String EX_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_EXCEPTION = "1110";
    /**
     * 参数解析异常
     */
    public static final String PARAMS_PARSE_EXCEPTION = "1111";
    /**
     * 参数不能为空
     */
    public static final String PARAMS_IS_NULL = "1112";
    //-----------------------------------------------------------------------------------
}