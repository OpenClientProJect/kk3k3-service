package com.ligg.admin.service;

import com.ligg.admin.dto.AdminInfoDTO;
import com.ligg.admin.dto.AdminLoginDTO;

/**
 * 管理员服务接口
 */
public interface AdminUserService {
    
    /**
     * 管理员登录
     * @param loginDTO 登录信息
     * @param ip 登录IP
     * @return 登录结果
     */
    AdminInfoDTO login(AdminLoginDTO loginDTO, String ip);
} 