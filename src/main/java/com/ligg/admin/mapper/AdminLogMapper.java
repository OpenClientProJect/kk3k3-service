package com.ligg.admin.mapper;

import com.ligg.admin.entity.AdminLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员日志Mapper接口
 */
@Mapper
public interface AdminLogMapper {
    /**
     * 插入管理员日志
     * @param log 日志信息
     * @return 影响行数
     */
    int insert(AdminLog log);
} 