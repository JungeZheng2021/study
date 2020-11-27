package com.study.auth.config.core.util;

/**
 * @Package: com.study.auth.config.core.util
 * @Description: <使用枚举可以保证线程安全>
 * @Author: milla
 * @CreateDate: 2020/09/11 17:48
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/11 17:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum AuthenticationStoreUtil {
    AUTHENTICATION;
    /**
     * 登录认证之后的token
     */
    private final ThreadLocal<String> tokenStore = new ThreadLocal<>();
    /**
     * 需要验证用户名
     */
    private final ThreadLocal<String> usernameStore = new ThreadLocal<>();
    /**
     * 需要验证的密码
     */
    private final ThreadLocal<String> passwordStore = new ThreadLocal<>();

    public static String getUsername() {
        return AUTHENTICATION.usernameStore.get();
    }

    public static void setUsername(String username) {
        AUTHENTICATION.usernameStore.set(username);
    }

    public static String getPassword() {
        return AUTHENTICATION.passwordStore.get();
    }

    public static void setPassword(String password) {
        AUTHENTICATION.passwordStore.set(password);
    }

    public static String getToken() {
        return AUTHENTICATION.tokenStore.get();
    }

    public static void setToken(String token) {
        AUTHENTICATION.tokenStore.set(token);
    }

    /**
     * 清除所有登录信息
     */
    public static void clear() {
        AUTHENTICATION.tokenStore.remove();
        AUTHENTICATION.passwordStore.remove();
        AUTHENTICATION.usernameStore.remove();
    }
}