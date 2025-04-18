package com.ligg.mapper;

import com.ligg.entity.Dynamic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DynamicMapper {
    /**
     * 插入动态
     */
    int insert(Dynamic dynamic);
    
    /**
     * 更新动态
     */
    int update(Dynamic dynamic);
    
    /**
     * 逻辑删除动态
     */
    int deleteById(Long id);
    
    /**
     * 更新点赞数
     */
    int updateLikeCount(@Param("id") Long id, @Param("increment") int increment);
    
    /**
     * 更新评论数
     */
    int updateCommentCount(@Param("id") Long id, @Param("increment") int increment);
    
    /**
     * 根据ID查询动态
     */
    Dynamic selectById(Long id);
    
    /**
     * 查询用户的动态列表
     */
    List<Dynamic> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询最新动态列表
     */
    List<Dynamic> selectLatest(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 统计用户动态数量
     */
    int countByUserId(Long userId);
    
    /**
     * 统计总动态数量
     */
    int countTotal();
} 