package com.aimsphm.nuclear.common.filter;

import com.aimsphm.nuclear.common.redis.RedisClient;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CorsFilter implements Filter {

    @Autowired
    RedisClient redisClient;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        try {
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authorization) && !redisClient.hasKey(authorization)) {
                Claim username = getNameFromToken(authorization);
                redisClient.set(authorization, username.asString(), 3600, TimeUnit.SECONDS);
            } else if (StringUtils.isNotBlank(authorization) && redisClient.hasKey(authorization)) {
                // extend expired time
                redisClient.set(authorization, redisClient.get(authorization), 3600, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "X-Requested-With, Content-Type, X-Codingpedia, Authorization");
        chain.doFilter(req, res);
    }

    private Claim getNameFromToken(String authorization) {
        DecodedJWT jwt = JWT.decode(authorization);
        Map<String, Claim> map = jwt.getClaims();
        Claim username = map.get("username");
        return username;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
