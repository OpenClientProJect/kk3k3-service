package com.ligg.admin.mapper;

import com.ligg.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 管理员视频Mapper接口
 */
@Mapper
public interface AdminVideoMapper {


    /**
     * 获取所有视频
     * @return 视频列表
     */
    List<Map<String, Object>> selectAllVideos();

    /**
     * 统计符合条件的视频数量
     *
     * @param status 状态筛选（可为null）
     * @param keyword 关键词（可为null）
     * @return 数量
     */
    int countAllVideos(
            @Param("status") Integer status,
            @Param("keyword") String keyword);

    /**
     * 获取视频详情
     *
     * @param id 视频ID
     * @return 视频详情
     */
    Map<String, Object> selectVideoById(@Param("id") Long id);

    /**
     * 删除视频
     *
     * @param id 视频ID
     * @return 影响行数
     */
    int deleteVideo(@Param("id") Long id);


    /**
     * 插入视频
     * @param video
     */
    void insert(Video video);
}