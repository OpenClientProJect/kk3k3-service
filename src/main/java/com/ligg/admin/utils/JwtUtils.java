package com.ligg.admin.utils;

import com.ligg.admin.config.JwtConfig;
import com.ligg.admin.entity.AdminUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 * JWT工具类
 */
@Component
public class JwtUtils {

    @Autowired
    private JwtConfig jwtConfig;
    
    // 用于缓存生成的密钥
    private SecretKey cachedKey;

    /**
     * 获取安全强度足够的密钥
     * @return SecretKey
     */
    private SecretKey getSigningKey() {
        if (cachedKey == null) {
            // 两种方式：
            // 1. 基于配置的密钥生成足够长度的密钥
            byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
            cachedKey = Keys.hmacShaKeyFor(keyBytes);
            
            // 2. 或使用推荐方式，直接生成适合HS512的随机密钥
            // cachedKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
        return cachedKey;
    }

    /**
     * 生成管理员token
     * @param admin 管理员信息
     * @return token
     */
    public String generateToken(AdminUser admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", admin.getId());
        claims.put("username", admin.getUsername());
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration() * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析token
     * @param token token
     * @return Claims
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验token是否有效
     * @param token token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        Claims claims = parseToken(token);
        return claims != null && !claims.getExpiration().before(new Date());
    }

    /**
     * 从token中获取管理员ID
     * @param token token
     * @return 管理员ID
     */
    public Long getAdminIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? Long.valueOf(claims.get("id").toString()) : null;
    }

    /**
     * 从token中获取用户名
     * @param token token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("username").toString() : null;
    }
} 