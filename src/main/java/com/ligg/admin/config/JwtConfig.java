package com.ligg.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置类
 */
@Component
@ConfigurationProperties(prefix = "admin.jwt")
public class JwtConfig {
    /**
     * 密钥
     */
    private String secret;
    
    /**
     * 过期时间（秒）
     */
    private long expiration;
    
    /**
     * 签发者
     */
    private String issuer;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
} 