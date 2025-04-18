package com.ligg.service;

import com.ligg.entity.Comment;
import java.util.List;
import java.util.Map;

/**
 * 评论服务接口
 */
public interface CommentService {
    
    /**
     * 添加评论
     * @param comment 评论对象
     * @return 添加结果
     */
    boolean addComment(Comment comment);
    
    /**
     * 回复评论
     * @param comment 评论对象
     * @return 回复结果
     */
    boolean replyComment(Comment comment);
    
    /**
     * 获取视频评论列表
     * @param videoId 视频ID
     * @param userId 当前用户ID（可为null）
     * @param page 页码
     * @param size 每页数量
     * @return 评论数据，包含评论列表和总数
     */
    Map<String, Object> getVideoComments(Long videoId, Long userId, int page, int size);
    
    /**
     * 删除评论
     * @param commentId 评论ID
     * @param currentUserId 当前用户ID
     * @return 删除结果
     */
    boolean deleteComment(Long commentId, Long currentUserId);
    
    /**
     * 点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 点赞结果
     */
    boolean likeComment(Long commentId, Long userId);
    
    /**
     * 取消点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 取消点赞结果
     */
    boolean unlikeComment(Long commentId, Long userId);
    
    /**
     * 检查用户是否已点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    boolean hasLiked(Long commentId, Long userId);
} 