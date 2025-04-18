package com.ligg.mapper;

import com.ligg.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论数据访问接口
 */
@Mapper
public interface CommentMapper {
    
    /**
     * 插入评论
     * @param comment 评论对象
     * @return 影响行数
     */
    int insert(Comment comment);
    
    /**
     * 根据ID查询评论
     * @param id 评论ID
     * @return 评论对象
     */
    Comment findById(Long id);
    
    /**
     * 根据视频ID查询顶级评论
     * @param videoId 视频ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 评论列表
     */
    List<Comment> findTopLevelByVideoId(@Param("videoId") Long videoId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据父评论ID查询回复
     * @param parentId 父评论ID
     * @return 回复列表
     */
    List<Comment> findRepliesByParentId(Long parentId);
    
    /**
     * 统计视频的评论数量
     * @param videoId 视频ID
     * @return 评论数量
     */
    int countByVideoId(Long videoId);
    
    /**
     * 更新评论
     * @param comment 评论对象
     * @return 影响行数
     */
    int update(Comment comment);
    
    /**
     * 删除评论（逻辑删除）
     * @param id 评论ID
     * @return 影响行数
     */
    int delete(Long id);
    
    /**
     * 增加评论点赞数
     * @param id 评论ID
     * @return 影响行数
     */
    int incrementLikes(Long id);
    
    /**
     * 减少评论点赞数
     * @param id 评论ID
     * @return 影响行数
     */
    int decrementLikes(Long id);
} 