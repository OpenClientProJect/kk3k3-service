package com.ligg.util;

import org.springframework.util.DigestUtils;

/**
 * 密码加密工具类
 */
public class PasswordEncoder {
    
    /**
     * 盐值，用于混淆
     */
    private static final String SALT = "PlayVideo@#$%^";
    
    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        // 加盐并使用MD5加密
        return DigestUtils.md5DigestAsHex((rawPassword + SALT).getBytes());
    }
    
    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        // 验证加密后的密码是否匹配
        return encodedPassword.equals(encode(rawPassword));
    }
} 