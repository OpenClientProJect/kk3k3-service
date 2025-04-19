package com.ligg.controller;

import com.ligg.entity.Comment;
import com.ligg.entity.User;
import com.ligg.service.CommentService;
import com.ligg.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comment")
@Slf4j
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    /**
     * 获取视频评论列表
     * @param videoId 视频ID
     * @param page 页码
     * @param size 每页数量
     * @param request HTTP请求
     * @return 评论列表和总数
     */
    @GetMapping("/video/{videoId}")
    public ResponseResult<Map<String, Object>> getVideoComments(
            @PathVariable Long videoId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            jakarta.servlet.http.HttpServletRequest request) {
        
        // 尝试获取当前登录用户
        User currentUser = (User) request.getAttribute("user");
        Long userId = currentUser != null ? currentUser.getId() : null;
        
        Map<String, Object> result = commentService.getVideoComments(videoId, userId, page, size);
        return ResponseResult.success(result);
    }
    
    /**
     * 发表评论
     * @param comment 评论信息
     * @param request HTTP请求
     * @return 发表结果
     */
    @PostMapping("")
    public ResponseResult<String> postComment(@RequestBody Comment comment, jakarta.servlet.http.HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return ResponseResult.error(401, "未登录");
        }
        
        comment.setUserId(currentUser.getId());
        boolean success = commentService.addComment(comment);
        
        if (success) {
            return ResponseResult.success("评论发表成功");
        } else {
            return ResponseResult.error("评论发表失败");
        }
    }
    
    /**
     * 回复评论
     * @param comment 回复信息
     * @param request HTTP请求
     * @return 回复结果
     */
    @PostMapping("/reply")
    public ResponseResult<String> replyComment(@RequestBody Comment comment, jakarta.servlet.http.HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return ResponseResult.error(401, "未登录");
        }
        
        if (comment.getParentId() == null) {
            return ResponseResult.error("父评论ID不能为空");
        }
        
        comment.setUserId(currentUser.getId());
        boolean success = commentService.replyComment(comment);
        
        if (success) {
            return ResponseResult.success("回复发表成功");
        } else {
            return ResponseResult.error("回复发表失败");
        }
    }
    
    /**
     * 删除评论
     * @param commentId 评论ID
     * @param request HTTP请求
     * @return 删除结果
     */
    @DeleteMapping("/{commentId}")
    public ResponseResult<String> deleteComment(@PathVariable Long commentId, jakarta.servlet.http.HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return ResponseResult.error(401, "未登录");
        }
        
        boolean success = commentService.deleteComment(commentId, currentUser.getId());
        
        if (success) {
            return ResponseResult.success("评论已删除");
        } else {
            return ResponseResult.error("删除评论失败，可能无权限或评论不存在");
        }
    }
}