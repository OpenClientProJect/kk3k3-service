package com.ligg.service.impl;

import com.ligg.entity.UserSubscription;
import com.ligg.mapper.UserMapper;
import com.ligg.mapper.UserSubscriptionMapper;
import com.ligg.service.UserSubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户关注/订阅服务实现类
 */
@Service
@Slf4j
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    
    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    @Transactional
    public boolean subscribe(Long userId, Long targetUserId) {
        // 不能关注自己
        if (userId.equals(targetUserId)) {
            log.warn("用户不能关注自己: userId={}", userId);
            return false;
        }
        
        // 检查是否已关注
        if (isSubscribed(userId, targetUserId)) {
            log.warn("用户已关注: userId={}, targetUserId={}", userId, targetUserId);
            return false;
        }
        
        // 创建关注记录
        UserSubscription subscription = new UserSubscription();
        subscription.setUserId(userId);
        subscription.setTargetUserId(targetUserId);
        subscription.setCreateTime(LocalDateTime.now());
        
        // 添加关注记录
        int rows = userSubscriptionMapper.insert(subscription);
        
        if (rows > 0) {
            // 更新用户的关注数
            userMapper.incrementSubscriptionCount(userId);
            // 更新目标用户的粉丝数
            userMapper.incrementSubscriberCount(targetUserId);
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public boolean unsubscribe(Long userId, Long targetUserId) {
        // 检查是否已关注
        if (!isSubscribed(userId, targetUserId)) {
            log.warn("用户未关注: userId={}, targetUserId={}", userId, targetUserId);
            return false;
        }
        
        // 删除关注记录
        int rows = userSubscriptionMapper.delete(userId, targetUserId);
        
        if (rows > 0) {
            // 更新用户的关注数
            userMapper.decrementSubscriptionCount(userId);
            // 更新目标用户的粉丝数
            userMapper.decrementSubscriberCount(targetUserId);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean isSubscribed(Long userId, Long targetUserId) {
        UserSubscription subscription = userSubscriptionMapper.findByUserIdAndTargetUserId(userId, targetUserId);
        return subscription != null;
    }
    
    @Override
    public List<UserSubscription> getSubscriptions(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        return userSubscriptionMapper.findByUserId(userId, offset, size);
    }
    
    @Override
    public List<UserSubscription> getSubscribers(Long targetUserId, int page, int size) {
        int offset = (page - 1) * size;
        return userSubscriptionMapper.findByTargetUserId(targetUserId, offset, size);
    }
    
    @Override
    public int countSubscriptions(Long userId) {
        return userSubscriptionMapper.countByUserId(userId);
    }
    
    @Override
    public int countSubscribers(Long targetUserId) {
        return userSubscriptionMapper.countByTargetUserId(targetUserId);
    }
} 