package com.ligg.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论点赞记录实体类
 */
@Data
public class CommentLike {
    
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * 评论ID
     */
    private Long commentId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 