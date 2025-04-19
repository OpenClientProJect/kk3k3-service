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
     * 根据用户ID查询所有视频列表（包括已发布和审核中的视频）
     * @param userId 用户ID
     * @return 视频列表
     */
    List<Video> getAllVideosByUserId(Long userId);
    
    /**
     * 统计用户上传的所有视频数量（包括已发布和审核中的视频）
     * @param userId 用户ID
     * @return 视频数量
     */
    int countAllVideosByUserId(Long userId);
    
    /**
     * 删除视频
     * @param id 视频ID
     * @return 是否成功
     */
    boolean deleteVideo(Long id);
    

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