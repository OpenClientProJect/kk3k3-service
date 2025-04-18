package com.ligg.admin.utils;

/**
 * 密码生成工具，用于生成管理员密码
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        // 生成默认密码：admin123
        String defaultPassword = "admin123";
        String encrypted = MD5Utils.encrypt(defaultPassword);
        
        System.out.println("原始密码: " + defaultPassword);
        System.out.println("MD5加密后: " + encrypted);
    }
} 