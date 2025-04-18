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
     * 获取待审核视频（包括草稿视频表中状态为审核中的视频）
     *
     * @param offset 偏移量
     * @param limit 数量限制
     * @return 视频列表
     */
    List<Map<String, Object>> selectPendingVideos(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计待审核视频数量
     *
     * @return 数量
     */
    int countPendingVideos();

    /**
     * 获取所有视频（可筛选）
     *
     * @param offset 偏移量
     * @param limit 数量限制
     * @param status 状态筛选（可为null）
     * @param keyword 关键词（可为null）
     * @return 视频列表
     */
    List<Map<String, Object>> selectAllVideos(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("status") Integer status,
            @Param("keyword") String keyword);

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
     * 审核草稿视频
     *
     * @param id 视频ID
     * @param status 审核状态
     * @param rejectReason 拒绝原因
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int updateDraftVideoStatus(
            @Param("id") Long id,
            @Param("status") Integer status,
            @Param("rejectReason") String rejectReason,
            @Param("updateTime") String updateTime);

    /**
     * 将通过审核的草稿视频移动到正式视频表
     *
     * @param id 草稿视频ID
     * @return 影响行数
     */
    int moveDraftToVideo(@Param("id") Long id);

    /**
     * 删除视频
     *
     * @param id 视频ID
     * @return 影响行数
     */
    int deleteVideo(@Param("id") Long id);

    /**
     * 删除草稿视频
     *
     * @param id 草稿视频ID
     * @return 影响行数
     */
    int deleteDraftVideo(@Param("id") Long id);

    /**
     * 插入视频
     * @param video
     */
    void insert(Video video);
}