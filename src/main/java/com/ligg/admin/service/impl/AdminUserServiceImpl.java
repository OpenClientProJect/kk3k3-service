package com.ligg.admin.service.impl;

import com.ligg.admin.dto.AdminInfoDTO;
import com.ligg.admin.dto.AdminLoginDTO;
import com.ligg.admin.entity.AdminLog;
import com.ligg.admin.entity.AdminUser;
import com.ligg.admin.mapper.AdminLogMapper;
import com.ligg.admin.mapper.AdminUserMapper;
import com.ligg.admin.service.AdminUserService;
import com.ligg.admin.utils.JwtUtils;
import com.ligg.admin.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员服务实现类
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;
    
    @Autowired
    private AdminLogMapper adminLogMapper;
    
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Transactional
    public AdminInfoDTO login(AdminLoginDTO loginDTO, String ip) {
        // 查询管理员信息
        AdminUser admin = adminUserMapper.selectByUsername(loginDTO.getUsername());
        if (admin == null) {
            return null;
        }
        
        // 校验密码（使用MD5加密）
        if (!MD5Utils.verify(loginDTO.getPassword(), admin.getPassword())) {
            return null;
        }
        
        // 检查账号状态
        if (admin.getStatus() != 1) {
            return null;
        }
        
        // 更新登录信息
        adminUserMapper.updateLoginInfo(admin.getId(), ip);
        
        // 记录登录日志
        AdminLog log = new AdminLog();
        log.setAdminId(admin.getId());
        log.setOperation("LOGIN");
        log.setContent("管理员登录");
        log.setIp(ip);
        adminLogMapper.insert(log);
        
        // 生成token
        String token = jwtUtils.generateToken(admin);
        
        // 封装返回结果
        AdminInfoDTO adminInfoDTO = new AdminInfoDTO();
        adminInfoDTO.setId(admin.getId());
        adminInfoDTO.setUsername(admin.getUsername());
        adminInfoDTO.setNickname(admin.getNickname());
        adminInfoDTO.setAvatar(admin.getAvatar());
        adminInfoDTO.setToken(token);
        
        return adminInfoDTO;
    }
} 