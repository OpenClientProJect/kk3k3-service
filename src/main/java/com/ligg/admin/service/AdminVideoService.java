package com.ligg.admin.service;

import com.ligg.entity.Video;

import java.util.Map;

/**
 * 管理员视频服务接口
 */
public interface AdminVideoService {


    /**
     * 将视频信息保存到草稿视频表（待审核）
     * @param video 视频对象
     * @return 保存后的视频对象
     */
    Video saveVideo(Video video);


    /**
     * 获取所有视频列表（可筛选）
     *
     * @param offset 偏移量
     * @param limit 数量限制
     * @param status 状态筛选（可为null）
     * @param keyword 关键词（可为null）
     * @return 视频列表及总数
     */
    Map<String, Object> getAllVideos(int offset, int limit, Integer status, String keyword);

    /**
     * 获取视频详情
     *
     * @param id 视频ID
     * @return 视频详情
     */
    Map<String, Object> getVideoDetail(Long id);



    /**
     * 删除视频
     *
     * @param id 视频ID
     * @return 是否成功
     */
    boolean deleteVideo(Long id);
} 