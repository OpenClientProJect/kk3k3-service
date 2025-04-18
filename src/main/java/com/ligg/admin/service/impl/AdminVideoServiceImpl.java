package com.ligg.admin.service.impl;

import com.ligg.admin.mapper.AdminVideoMapper;
import com.ligg.admin.service.AdminVideoService;
import com.ligg.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public Map<String, Object> getAllVideos(int offset, int limit, Integer status, String keyword) {
        List<Map<String, Object>> videos = adminVideoMapper.selectAllVideos(offset, limit, status, keyword);
        int total = adminVideoMapper.countAllVideos(status, keyword);
        
        Map<String, Object> result = new HashMap<>();
        result.put("videos", videos);
        result.put("total", total);
        
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
        // 如果没有删除到正式视频，尝试删除草稿视频
        if (rows == 0) {
            rows = adminVideoMapper.deleteDraftVideo(id);
        }
        return rows > 0;
    }
} 