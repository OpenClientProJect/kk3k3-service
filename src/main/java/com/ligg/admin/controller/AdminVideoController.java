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
     * 获取所有视频列表（可筛选）
     *
     * @param page 页码
     * @param size 每页数量
     * @param status 状态筛选
     * @param keyword 关键词搜索
     * @return 视频列表和总数
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> getAllVideos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        int offset = (page - 1) * size;
        Map<String, Object> result = adminVideoService.getAllVideos(offset, size, status, keyword);
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