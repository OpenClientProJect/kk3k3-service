package com.ligg.mapper;

import com.ligg.entity.CommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评论点赞记录数据访问接口
 */
@Mapper
public interface CommentLikeMapper {
    
    /**
     * 插入点赞记录
     * @param commentLike 点赞记录对象
     * @return 影响行数
     */
    int insert(CommentLike commentLike);
    
    /**
     * 删除点赞记录
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int delete(@Param("commentId") Long commentId, @Param("userId") Long userId);
    
    /**
     * 检查用户是否已点赞
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 点赞记录数量
     */
    int countByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);
} 