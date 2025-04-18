package com.ligg.service;

import com.ligg.entity.Dynamic;
import com.ligg.entity.DynamicComment;
import java.util.Map;

/**
 * 动态服务接口
 */
public interface DynamicService {
    /**
     * 发布动态
     */
    Dynamic publishDynamic(Long userId, String content, String images, Long videoId);
    
    /**
     * 删除动态
     */
    boolean deleteDynamic(Long dynamicId, Long userId);
    
    /**
     * 点赞/取消点赞动态
     */
    boolean likeDynamic(Long dynamicId, Long userId);
    
    /**
     * 判断用户是否已点赞动态
     */
    boolean isLiked(Long dynamicId, Long userId);
    
    /**
     * 获取动态详情
     */
    Map<String, Object> getDynamicDetail(Long dynamicId, Long currentUserId);
    
    /**
     * 获取用户动态列表
     */
    Map<String, Object> getUserDynamics(Long userId, int page, int size);
    
    /**
     * 获取最新动态
     */
    Map<String, Object> getLatestDynamics(int page, int size, Long currentUserId);
    
    /**
     * 发表评论
     */
    DynamicComment comment(Long dynamicId, Long userId, String content, Long parentId);
    
    /**
     * 获取动态评论列表
     */
    Map<String, Object> getDynamicComments(Long dynamicId, int page, int size);
    
    /**
     * 删除评论
     */
    boolean deleteComment(Long commentId, Long userId);
} 