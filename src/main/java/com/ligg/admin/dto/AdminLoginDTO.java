package com.ligg.admin.dto;

import lombok.Data;

/**
 * 管理员登录请求DTO
 */
@Data
public class AdminLoginDTO {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
} 