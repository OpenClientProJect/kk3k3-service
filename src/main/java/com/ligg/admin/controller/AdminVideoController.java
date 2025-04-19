package com.ligg.admin.controller;

import com.ligg.admin.service.AdminVideoService;
import com.ligg.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员视频控制器
 */
@RestController
@RequestMapping("/api/admin/video")
public class AdminVideoController {

    @Autowired
    private AdminVideoService adminVideoService;


    /**
     * 获取所有视频列表
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> getAllVideos(){
        Map<String, Object> result = adminVideoService.getAllVideos();
        return ResponseResult.success(result);
    }

    /**
     * 获取视频详情
     *
     * @param id 视频ID
     * @return 视频详情
     */
    @GetMapping("/detail/{id}")
    public ResponseResult<Map<String, Object>> getVideoDetail(@PathVariable Long id) {
        Map<String, Object> videoDetail = adminVideoService.getVideoDetail(id);
        return ResponseResult.success(videoDetail);
    }


    /**
     * 删除视频
     *
     * @param id 视频ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteVideo(@PathVariable Long id) {
        boolean success = adminVideoService.deleteVideo(id);
        if (success) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.error("删除失败");
        }
    }
} 