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
    

    @Override
    public HashMap<String, Object> getVideoById(Long id) {
        HashMap<String, Object> videoInfoMap = new HashMap<>();
        Video video = videoMapper.selectById(id);
        videoInfoMap.put("video", video);
        return videoInfoMap;
    }
    

    @Override
    public List<Video> getAllVideosByUserId(Long userId) {
        // 获取正式视频
        return videoMapper.selectByUserId(userId);
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
    public List<Video> getLatestVideos() {
        return videoMapper.selectLatestVideos();
    }

    
    @Override
    public List<Video> searchVideos(String keyword, int offset, int limit) {
        return videoMapper.searchByKeyword(keyword, 1, offset, limit);
    }
    

    @Override
    public int countSearchResults(String keyword) {
        return videoMapper.countSearchResult(keyword, 1);
    }
}