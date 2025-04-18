package com.ligg.mapper;

import com.ligg.entity.DynamicLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DynamicLikeMapper {
    /**
     * 插入点赞记录
     */
    int insert(DynamicLike dynamicLike);
    
    /**
     * 删除点赞记录
     */
    int deleteByDynamicIdAndUserId(@Param("dynamicId") Long dynamicId, @Param("userId") Long userId);
    
    /**
     * 查询点赞记录
     */
    DynamicLike selectByDynamicIdAndUserId(@Param("dynamicId") Long dynamicId, @Param("userId") Long userId);
    
    /**
     * 统计动态点赞数
     */
    int countByDynamicId(Long dynamicId);
} 