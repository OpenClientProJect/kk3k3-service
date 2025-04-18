package com.ligg.admin.dto;

import lombok.Data;

/**
 * 管理员信息DTO
 */
@Data
public class AdminInfoDTO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String token;
} 