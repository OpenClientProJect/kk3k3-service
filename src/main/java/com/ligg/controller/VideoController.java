package com.ligg.controller;

import com.ligg.entity.Video;
import com.ligg.service.VideoService;
import com.ligg.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 获取视频列表
     */
    @GetMapping("/latest")
    public ResponseResult<Map<String, Object>> getLatestVideos() {
        List<Video> videos = videoService.getLatestVideos();
        Map<String, Object> result = new HashMap<>();
        result.put("videos", videos);
        return ResponseResult.success(result);
    }


    /**
     * 根据分类获取视频列表
     *
     * @param category 分类名称
     * @param page     页码
     * @param size     每页大小
     * @return 分页视频列表
     */
    @GetMapping("/category")
    public ResponseResult<Map<String, Object>> getVideosByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        int offset = (page - 1) * size;
        List<Video> videos = videoService.getVideosByCategory(category, offset, size);
        int total = videoService.countVideosByCategory(category);

        Map<String, Object> result = new HashMap<>();
        result.put("videos", videos);
        result.put("total", total);
        result.put("currentPage", page);
        result.put("pageSize", size);
        result.put("category", category);

        return ResponseResult.success(result);
    }

    /**
     * 搜索视频
     *
     * @param keyword 关键词
     * @param page    页码
     * @param size    每页大小
     * @return 分页视频列表
     */
    @GetMapping("/search")
    public ResponseResult<Map<String, Object>> searchVideos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {
        int offset = (page - 1) * size;
        List<Video> videos = videoService.searchVideos(keyword, offset, size);
        int total = videoService.countSearchResults(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("videos", videos);
        result.put("total", total);
        result.put("currentPage", page);
        result.put("pageSize", size);
        result.put("keyword", keyword);

        return ResponseResult.success(result);
    }

    /**
     * 获取视频详情
     *
     * @param id 视频ID
     * @return 视频详情
     */
    @GetMapping("/{id}")
    public ResponseResult<HashMap<String, Object>> getVideoDetail(@PathVariable Long id) {
        HashMap<String, Object> videoInfo = videoService.getVideoById(id);
        if (videoInfo == null) {
            return ResponseResult.error("视频不存在");
        }
        return ResponseResult.success(videoInfo);
    }
}
