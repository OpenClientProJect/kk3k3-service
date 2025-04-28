package com.ligg.admin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置类
 */
@Setter
@Getter
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

}