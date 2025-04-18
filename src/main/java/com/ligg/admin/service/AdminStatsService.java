package com.ligg.admin.service;

import java.util.Map;

/**
 * 管理员统计服务接口
 */
public interface AdminStatsService {

    /**
     * 获取统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getStats();
} 