package com.ligg.admin.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class MD5Utils {
    
    /**
     * MD5加密
     * @param input 输入字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String input) {
        try {
            // 获取MD5加密对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // 加密处理
            byte[] messageDigest = md.digest(input.getBytes());
            
            // 转换为16进制字符串
            BigInteger number = new BigInteger(1, messageDigest);
            StringBuilder sb = new StringBuilder(number.toString(16));
            
            // 填充至32位
            while (sb.length() < 32) {
                sb.insert(0, "0");
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
    
    /**
     * 验证密码
     * @param raw 原始密码
     * @param encrypted 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String raw, String encrypted) {
        return encrypt(raw).equals(encrypted);
    }

    public static void main(String[] args) {
        System.out.printf(MD5Utils.encrypt("1111111"));
    }
} 