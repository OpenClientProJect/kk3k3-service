package com.ligg.admin.mapper;

import com.ligg.admin.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员Mapper接口
 */
@Mapper
public interface AdminUserMapper {
    /**
     * 根据用户名查询管理员信息
     * @param username 用户名
     * @return 管理员信息
     */
    AdminUser selectByUsername(@Param("username") String username);
    
    /**
     * 更新管理员登录信息
     * @param id 管理员ID
     * @param ip 登录IP
     * @return 影响行数
     */
    int updateLoginInfo(@Param("id") Long id, @Param("ip") String ip);
} 