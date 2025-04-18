package com.ligg.admin.service.impl;

import com.ligg.admin.mapper.AdminStatsMapper;
import com.ligg.admin.service.AdminStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员统计服务实现类
 */
@Service
public class AdminStatsServiceImpl implements AdminStatsService {

    @Autowired
    private AdminStatsMapper adminStatsMapper;

    /**
     * 获取统计数据
     */
    @Override
    public Map<String, Object> getStats() {
        // 尝试使用一次性获取所有统计数据的方法
        try {
            Map<String, Object> allStats = adminStatsMapper.getAllStats();
            if (allStats != null) {
                // 补充其他统计数据
                allStats.put("lastWeekVideos", adminStatsMapper.getLastWeekVideos());
                allStats.put("lastWeekUsers", adminStatsMapper.getLastWeekUsers());
                allStats.put("categoryStats", adminStatsMapper.getVideoCategoryStats());
                allStats.put("topUsers", adminStatsMapper.getTopActiveUsers());
                allStats.put("topVideos", adminStatsMapper.getTopVideos());
                return allStats;
            }
        } catch (Exception e) {
            // 如果发生异常，则回退到单独获取每个统计数据
        }
        
        // 单独获取各项统计数据
        Map<String, Object> stats = new HashMap<>();
        
        // 获取视频总数
        int totalVideos = adminStatsMapper.getTotalVideos();
        stats.put("totalVideos", totalVideos);
        
        // 获取待审核视频数量
        int pendingVideos = adminStatsMapper.getPendingVideos();
        stats.put("pendingVideos", pendingVideos);
        
        // 获取用户总数
        int totalUsers = adminStatsMapper.getTotalUsers();
        stats.put("totalUsers", totalUsers);
        
        // 获取今日新增视频数
        int todayNewVideos = adminStatsMapper.getTodayNewVideos();
        stats.put("todayNewVideos", todayNewVideos);
        
        // 获取今日新增用户数
        int todayNewUsers = adminStatsMapper.getTodayNewUsers();
        stats.put("todayNewUsers", todayNewUsers);
        
        // 获取最近7天每天新增视频数量
        stats.put("lastWeekVideos", adminStatsMapper.getLastWeekVideos());
        
        // 获取最近7天每天新增用户数量
        stats.put("lastWeekUsers", adminStatsMapper.getLastWeekUsers());
        
        // 获取视频分类统计
        stats.put("categoryStats", adminStatsMapper.getVideoCategoryStats());
        
        // 获取最活跃的TOP 10用户
        stats.put("topUsers", adminStatsMapper.getTopActiveUsers());
        
        // 获取最热门的TOP 10视频
        stats.put("topVideos", adminStatsMapper.getTopVideos());
        
        return stats;
    }
} 