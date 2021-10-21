package com.aimsphm.nuclear.common.constant;

public class AuthConstant {
    public static final Long EXPIRATIONTIME = 43_200_000_000L;     // 500 days
    public static final String SECRET = "P@ssw0rd_SM";            // JWT password
    public static final String TOKEN_PREFIX = "Bearer";        // Token prefix
    public static final String AUTH_HEADER_STRING = "Authorization";// header key for authorization
    public static final String AUTHORITIES = "authorities";
}
