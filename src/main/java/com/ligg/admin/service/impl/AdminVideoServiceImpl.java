package com.ligg.admin.service.impl;

import com.ligg.admin.mapper.AdminVideoMapper;
import com.ligg.admin.service.AdminVideoService;
import com.ligg.entity.Episodes;
import com.ligg.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员视频服务实现类
 */
@Service
public class AdminVideoServiceImpl implements AdminVideoService {

    @Autowired
    private AdminVideoMapper adminVideoMapper;


    @Override
    @Transactional
    public Video saveVideo(Video video) {
            // 执行插入视频
            adminVideoMapper.insert(video);
        return video;
    }
    /**
     * 获取所有视频列表（可筛选）
     */
    @Override
    public Map<String, Object> getAllVideos() {
        List<Map<String, Object>> videos = adminVideoMapper.selectAllVideos();

        Map<String, Object> result = new HashMap<>();
        result.put("videos", videos);
        return result;
    }

    /**
     * 获取视频详情
     */
    @Override
    public Map<String, Object> getVideoDetail(Long id) {
        return adminVideoMapper.selectVideoById(id);
    }



    /**
     * 删除视频
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVideo(Long id) {
        // 先尝试删除正式视频
        int rows = adminVideoMapper.deleteVideo(id);
        return rows > 0;
    }

//    @Override
//    public void addEpisode(Episodes episode) {
//        adminVideoMapper.insertEpisode(episode);
//    }

    /**
     * 插入剧集
     */
    @Override
    @Transactional
    public void insertEpisode(Episodes episode) {
        adminVideoMapper.insertEpisode(episode);
    }
}