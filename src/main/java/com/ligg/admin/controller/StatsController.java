package com.ligg.admin.controller;

import com.ligg.admin.service.AdminStatsService;
import com.ligg.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 管理员统计控制器
 */
@RestController
@RequestMapping("/api/admin")
public class StatsController {

    @Autowired
    private AdminStatsService adminStatsService;

    /**
     * 获取统计数据
     *
     * @return 统计数据
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getStats() {
        Map<String, Object> stats = adminStatsService.getStats();
        return ResponseResult.success(stats);
    }
} 