package com.ligg.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户关注/订阅实体类
 */
@Data
public class UserSubscription {
    
    /**
     * 关注ID
     */
    private Long id;
    
    /**
     * 关注者ID
     */
    private Long userId;
    
    /**
     * 被关注者ID
     */
    private Long targetUserId;
    
    /**
     * 关注时间
     */
    private LocalDateTime createTime;
} 