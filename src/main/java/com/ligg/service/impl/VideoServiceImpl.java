package com.ligg.service.impl;

import com.ligg.entity.Episodes;
import com.ligg.entity.Video;
import com.ligg.mapper.VideoMapper;
import com.ligg.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Episodes> episodes = videoMapper.getEpisodes(id);
        videoInfoMap.put("episodes", episodes);
        return videoInfoMap;
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