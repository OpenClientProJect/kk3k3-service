package com.ligg.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论实体类
 */
@Data
public class Comment {
    
    /**
     * 评论ID
     */
    private Long id;
    
    /**
     * 视频ID
     */
    private Long videoId;
    
    /**
     * 评论用户ID
     */
    private Long userId;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 点赞数
     */
    private Integer likes;
    
    /**
     * 父评论ID，若为顶级评论则为null
     */
    private Long parentId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 状态：0-删除，1-正常
     */
    private Integer status;
    
    /**
     * 用户名称（非数据库字段）
     */
    private String username;
    
    /**
     * 用户头像URL（非数据库字段）
     */
    private String userAvatar;

    private Integer replyToUserId;
    
    /**
     * 回复列表（非数据库字段）
     */
    private List<Comment> replies;
    
    /**
     * 当前用户是否已点赞（非数据库字段）
     */
    private Boolean liked;
} 