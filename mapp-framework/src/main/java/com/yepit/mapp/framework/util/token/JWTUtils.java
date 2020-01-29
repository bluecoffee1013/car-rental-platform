package com.yepit.mapp.framework.util.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianlong on 2017/10/15.
 */
//@Configuration
@Component
@ConfigurationProperties(prefix="token")
public class JWTUtils {

    private final Logger logger = Logger.getLogger(JWTUtils.class);

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration}")
    private Long expiration;

    /**
     * 根据 TokenDetail 生成 Token
     *
     * @param tokenDetail
     * @return
     */
    public String generateToken(TokenDetail tokenDetail) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", tokenDetail.getUsername());
        claims.put("created", this.generateCurrentDate());
        return this.generateToken(claims);
    }

    /**
     * 根据 claims 生成 Token
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(this.generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, this.secret.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            //didn't want to have this method throw the exception, would rather log it and sign the token like it was before
            logger.warn(ex.getMessage());
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(this.generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, this.secret)
                    .compact();
        }
    }

    /**
     * token 过期时间
     *
     * @return
     */
    private Date generateExpirationDate() {

        return new Date(System.currentTimeMillis() + this.expiration * 1000);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 从 token 中拿到 username
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 解析 token 的主体 Claims
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.secret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
