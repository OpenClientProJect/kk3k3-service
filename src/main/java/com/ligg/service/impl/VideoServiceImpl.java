package com.ligg.service.impl;

import com.ligg.entity.User;
import com.ligg.entity.Video;
import com.ligg.mapper.DraftVideoMapper;
import com.ligg.mapper.UserMapper;
import com.ligg.mapper.VideoMapper;
import com.ligg.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 视频服务实现类
 */
@Service
public class VideoServiceImpl implements VideoService {
    
    @Autowired
    private VideoMapper videoMapper;
    
    @Autowired
    private DraftVideoMapper draftVideoMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Video saveVideo(Video video) {
        // 如果是新视频，设置创建时间
        if (video.getId() == null) {
            video.setCreateTime(LocalDateTime.now());
            // 更新时间
            video.setUpdateTime(LocalDateTime.now());
            // 执行插入
            videoMapper.insert(video);
        } else {
            // 更新时间
            video.setUpdateTime(LocalDateTime.now());
            // 执行更新
            videoMapper.update(video);
        }
        
        return video;
    }
    
    @Override
    @Transactional
    public Video saveToDraftVideo(Video video) {
        // 如果是新视频，设置创建时间
        if (video.getId() == null) {
            video.setCreateTime(LocalDateTime.now());
            // 更新时间
            video.setUpdateTime(LocalDateTime.now());
            // 执行插入到草稿表
            draftVideoMapper.insert(video);
        } else {
            // 更新时间
            video.setUpdateTime(LocalDateTime.now());
            // 执行更新草稿表
            draftVideoMapper.update(video);
        }
        
        return video;
    }
    
    @Override
    public HashMap<String, Object> getVideoById(Long id) {
        HashMap<String, Object> videoInfoMap = new HashMap<>();
        Video video = videoMapper.selectById(id);
        videoInfoMap.put("video", video);

        User userInfo = userMapper.findById(video.getUserId());
        videoInfoMap.put("userInfo", userInfo);
        return videoInfoMap;
    }
    
    @Override
    public List<Video> getVideosByUserId(Long userId, int offset, int limit) {
        return videoMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Video> getAllVideosByUserId(Long userId) {
        // 获取正式视频
        return videoMapper.selectByUserId(userId);
    }
    
    @Override
    public int countVideosByUserId(Long userId) {
        return videoMapper.countByUserId(userId);
    }
    
    @Override
    public int countAllVideosByUserId(Long userId) {
        // 正式视频数量
        int videoCount = videoMapper.countByUserId(userId);

        
        return videoCount ;
    }
    
    @Override
    @Transactional
    public boolean deleteVideo(Long id) {
        return videoMapper.deleteById(id) > 0;
    }
    
    @Override
    @Transactional
    public boolean incrementViews(Long id) {
        return videoMapper.incrementViews(id) > 0;
    }
    
    @Override
    @Transactional
    public boolean incrementLikes(Long id) {
        return videoMapper.incrementLikes(id) > 0;
    }
    
    @Override
    public List<Video> getVideosByCategory(String category, int offset, int limit) {
        return videoMapper.selectByCategory(category, 1, offset, limit);
    }
    
    @Override
    public List<Video> getLatestVideos(int offset, int limit) {
        return videoMapper.selectLatestVideos(1, offset, limit);
    }
    
    @Override
    public List<Video> getPopularVideos(int offset, int limit) {
        return videoMapper.selectPopularVideos(1, offset, limit);
    }
    
    @Override
    public List<Video> searchVideos(String keyword, int offset, int limit) {
        return videoMapper.searchByKeyword(keyword, 1, offset, limit);
    }
    
    @Override
    public int countVideos() {
        return videoMapper.countByStatus(1);
    }
    
    @Override
    public int countVideosByCategory(String category) {
        return videoMapper.countByCategory(category, 1);
    }
    
    @Override
    public int countSearchResults(String keyword) {
        return videoMapper.countSearchResult(keyword, 1);
    }
}