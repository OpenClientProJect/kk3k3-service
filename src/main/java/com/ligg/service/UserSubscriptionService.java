package com.ligg.service;

import com.ligg.entity.UserSubscription;
import java.util.List;

/**
 * 用户关注/订阅服务接口
 */
public interface UserSubscriptionService {
    
    /**
     * 关注用户
     * 
     * @param userId 关注者ID
     * @param targetUserId 被关注者ID
     * @return 是否成功
     */
    boolean subscribe(Long userId, Long targetUserId);
    
    /**
     * 取消关注
     * 
     * @param userId 关注者ID
     * @param targetUserId 被关注者ID
     * @return 是否成功
     */
    boolean unsubscribe(Long userId, Long targetUserId);
    
    /**
     * 检查是否已关注
     * 
     * @param userId 关注者ID
     * @param targetUserId 被关注者ID
     * @return 是否已关注
     */
    boolean isSubscribed(Long userId, Long targetUserId);
    
    /**
     * 获取用户的关注列表
     * 
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 关注列表
     */
    List<UserSubscription> getSubscriptions(Long userId, int page, int size);
    
    /**
     * 获取用户的粉丝列表
     * 
     * @param targetUserId 被关注的用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 粉丝列表
     */
    List<UserSubscription> getSubscribers(Long targetUserId, int page, int size);
    
    /**
     * 统计用户关注数
     * 
     * @param userId 用户ID
     * @return 关注数
     */
    int countSubscriptions(Long userId);
    
    /**
     * 统计用户粉丝数
     * 
     * @param targetUserId 被关注的用户ID
     * @return 粉丝数
     */
    int countSubscribers(Long targetUserId);
} 