package com.ligg.service;

import com.ligg.entity.Video;

import java.util.HashMap;
import java.util.List;

/**
 * 视频服务接口
 */
public interface VideoService {
    
    /**
     * 根据ID查询视频
     *
     * @param id 视频ID
     * @return 视频对象
     */
    HashMap<String, Object> getVideoById(Long id);
    

    /**
     * 获取视频列表
     * @return 视频列表
     */
    List<Video> getLatestVideos();
    

    /**
     * 搜索视频
     * @param keyword 关键词
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 视频列表
     */
    List<Video> searchVideos(String keyword, int offset, int limit);
    
    /**
     * 统计搜索结果数量
     * @param keyword 关键词
     * @return 视频数量
     */
    int countSearchResults(String keyword);

}