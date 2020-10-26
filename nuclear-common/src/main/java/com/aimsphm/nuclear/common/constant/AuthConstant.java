package com.aimsphm.nuclear.common.constant;

public class AuthConstant {
    public static  Long EXPIRATIONTIME = 43_200_000_000L;     // 500 days
    public static  String SECRET = "P@ssw0rd_SM";            // JWT password
    public static  String TOKEN_PREFIX = "Bearer";        // Token prefix
    public static  String AUTH_HEADER_STRING = "Authorization";// header key for authorization
    public static  String AUTHORITIES = "authorities";
}
