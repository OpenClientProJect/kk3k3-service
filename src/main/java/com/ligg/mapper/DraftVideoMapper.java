package com.ligg.mapper;

import com.ligg.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 草稿视频数据访问接口
 */
@Mapper
public interface DraftVideoMapper {
    
    /**
     * 将视频插入到草稿表
     * @param video 视频对象
     * @return 影响的行数
     */
    int insert(Video video);
    
    /**
     * 更新草稿视频
     * @param video 视频对象
     * @return 影响的行数
     */
    int update(Video video);
    
    /**
     * 根据ID查询草稿视频
     * @param id 视频ID
     * @return 视频对象
     */
    Video selectById(Long id);
    
    /**
     * 根据用户ID查询草稿视频列表
     * @param userId 用户ID
     * @param offset 起始行
     * @param limit 查询行数
     * @return 视频列表
     */
    List<Video> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据用户ID统计草稿视频数量
     * @param userId 用户ID
     * @return 视频数量
     */
    int countByUserId(Long userId);
    
    /**
     * 根据ID删除草稿视频
     * @param id 视频ID
     * @return 影响的行数
     */
    int deleteById(Long id);
} 