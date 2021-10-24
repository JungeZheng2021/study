package com.aimsphm.nuclear.common.token;

import com.aimsphm.nuclear.common.constant.AuthConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public class TokenAuthenticationService {
    private TokenAuthenticationService() {
    }

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);

    public static String createToken(String username, String locale, String authorities) {
        // 生成JWT
        return Jwts.builder()
                .claim("authorities", authorities)
                .setSubject(username)
                .claim("locale", locale)
                .setExpiration(new Date(System.currentTimeMillis() + AuthConstant.EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, AuthConstant.SECRET)
                .compact();
    }

    public static String getUserName(HttpServletRequest request) {
        Authentication authentication = getAuthentication(request);
        return authentication == null ? "" : (String) authentication.getPrincipal();
    }

    // JWT验证方法
    public static Authentication getAuthentication(HttpServletRequest request) {
        logger.debug("JWT验证");
        // get token from header
        String token = request.getHeader(AuthConstant.AUTH_HEADER_STRING);

        if (token != null) {
            // parse Token
            Claims claims = Jwts.parser()
                    // get sign key
                    .setSigningKey(AuthConstant.SECRET)
                    // get ride of Bearer
                    .parseClaimsJws(token.replace(AuthConstant.TOKEN_PREFIX, ""))
                    .getBody();

            // get user name
            String user = claims.getSubject();
            // get authorities
            String roleIdStr = claims.get("authorities").toString();
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roleIdStr);

            // return token
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, authorities) :
                    null;
        }
        return null;
    }
}
