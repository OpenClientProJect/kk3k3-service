package com.ligg.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 管理员统计Mapper接口
 */
@Mapper
public interface AdminStatsMapper {

    /**
     * 获取视频总数
     *
     * @return 视频总数
     */
    int getTotalVideos();
    
    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    int getTotalUsers();
    

    
    /**
     * 获取今日新增视频数
     *
     * @return 今日新增视频数
     */
    int getTodayNewVideos();
    
    /**
     * 获取今日新增用户数
     *
     * @return 今日新增用户数
     */
    int getTodayNewUsers();
    
    /**
     * 获取最近7天每天新增视频数量
     *
     * @return 最近7天每天新增视频数量列表
     */
    List<Map<String, Object>> getLastWeekVideos();
    
    /**
     * 获取最近7天每天新增用户数量
     *
     * @return 最近7天每天新增用户数量列表
     */
    List<Map<String, Object>> getLastWeekUsers();
    
    /**
     * 获取视频分类统计
     *
     * @return 视频分类统计列表
     */
    List<Map<String, Object>> getVideoCategoryStats();
    

    /**
     * 获取最热门的TOP 10视频
     *
     * @return 最热门视频列表
     */
    List<Map<String, Object>> getTopVideos();
    
    /**
     * 获取所有统计数据
     *
     * @return 所有统计数据
     */
    Map<String, Object> getAllStats();
} 