package com.aimsphm.nuclear.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * @Package: com.aimsphm.nuclear.common.util
 * @Description: <身份认证工具类>
 * @Author: MILLA
 * @CreateDate: 2020/3/2 10:55
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/2 10:55
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public final class AuthenticationUtils {

    /**
     * @param ip       ip地址
     * @param port     端口
     * @param domain   域名
     * @param username 账户名
     * @param password 账户明码
     * @return 上下文对象
     */
    public static DirContext authenticateByLdap(String ip, Integer port, String domain, String username, String password) {
        Assert.hasText(ip, "ip can not be null");
        Assert.isNull(port, "port can not be null");
        Assert.hasText(domain, "domain can not be null");
        Assert.hasText(username, "username can not be null");
        Assert.hasText(password, "password can not be null");
        // LDAP正式库地址
        String LDAP_URL = "ldap://" + ip + ":" + port;

        // 注意用户名的写法：domain\User或
        String adminName = domain + "\\" + username;
        log.debug("admin login " + adminName + " " + password);
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext dc = null;
        try {
            dc = new InitialDirContext(env);
        } catch (NamingException e) {
            log.error(e.getMessage());
        }
        return dc;
    }
}
