package com.ligg.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 视频实体类
 */
@Data
public class Video {
    
    private Long id;
    
    /**
     * 视频标题
     */
    private String title;
    
    /**
     * 视频描述
     */
    private String description;

    /**
     * 视频存储路径
     */
    private String videoPath;
    
    /**
     * 视频访问URL
     */
    private String videoUrl;
    
    /**
     * 封面存储路径
     */
    private String coverPath;
    
    /**
     * 封面URL
     */
    private String coverUrl;

    /**
     * 视频时长（秒）
     */
    private Integer duration;
    
    /**
     * 视频类别
     */
    private String category;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 视频标签，使用逗号分隔存储
     */
    private String tags;
    
    /**
     * 将标签字符串转换为列表
     * @return 标签列表
     */
    public List<String> getTagList() {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(tags.split(","));
    }
    
    /**
     * 设置标签列表
     * @param tagList 标签列表
     */
    public void setTagList(List<String> tagList) {
        if (tagList == null || tagList.isEmpty()) {
            this.tags = null;
            return;
        }
        this.tags = String.join(",", tagList);
    }
} 