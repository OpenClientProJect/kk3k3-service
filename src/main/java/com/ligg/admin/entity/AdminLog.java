package com.ligg.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 管理员操作日志实体类
 */
@Data
public class AdminLog {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 管理员ID
     */
    private Long adminId;
    
    /**
     * 操作类型
     */
    private String operation;
    
    /**
     * 操作内容
     */
    private String content;
    
    /**
     * 操作IP
     */
    private String ip;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 