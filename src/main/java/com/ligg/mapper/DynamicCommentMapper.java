package com.ligg.mapper;

import com.ligg.entity.DynamicComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DynamicCommentMapper {
    /**
     * 插入评论
     */
    int insert(DynamicComment comment);
    
    /**
     * 更新评论
     */
    int update(DynamicComment comment);
    
    /**
     * 逻辑删除评论
     */
    int deleteById(Long id);
    
    /**
     * 根据ID查询评论
     */
    DynamicComment selectById(Long id);
    
    /**
     * 查询动态的评论列表（主评论）
     */
    List<DynamicComment> selectByDynamicId(@Param("dynamicId") Long dynamicId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询评论的回复列表
     */
    List<DynamicComment> selectRepliesByParentId(Long parentId);
    
    /**
     * 统计动态的评论数
     */
    int countByDynamicId(Long dynamicId);
} 