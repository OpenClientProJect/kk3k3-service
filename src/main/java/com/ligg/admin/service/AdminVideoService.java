package com.ligg.admin.service;

import com.ligg.entity.Episodes;
import com.ligg.entity.Video;

import java.util.Map;

/**
 * 管理员视频服务接口
 */
public interface AdminVideoService {

    /**
     * 保存视频信息
     * @param video 视频信息
     * @return 保存的视频
     */
    Video saveVideo(Video video);

    /**
     * 获取所有视频
     * @return 视频列表
     */
    Map<String, Object> getAllVideos();

    /**
     * 获取视频详情
     * @param id 视频ID
     * @return 视频详情
     */
    Map<String, Object> getVideoDetail(Long id);

    /**
     * 删除视频
     * @param id 视频ID
     * @return 是否成功
     */
    boolean deleteVideo(Long id);
    
    /**
     * 插入剧集
     * @param episode 剧集信息
     */
    void insertEpisode(Episodes episode);
}