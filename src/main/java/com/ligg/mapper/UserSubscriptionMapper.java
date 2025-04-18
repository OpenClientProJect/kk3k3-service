package com.ligg.mapper;

import com.ligg.entity.UserSubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户关注/订阅数据访问接口
 */
@Mapper
public interface UserSubscriptionMapper {
    
    /**
     * 添加关注
     * 
     * @param subscription 关注对象
     * @return 影响的行数
     */
    int insert(UserSubscription subscription);
    
    /**
     * 取消关注
     * 
     * @param userId 用户ID
     * @param targetUserId 目标用户ID
     * @return 影响的行数
     */
    int delete(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);
    
    /**
     * 检查是否已关注
     * 
     * @param userId 用户ID
     * @param targetUserId 目标用户ID
     * @return 关注对象，不存在则为null
     */
    UserSubscription findByUserIdAndTargetUserId(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);
    
    /**
     * 获取用户的关注列表
     * 
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 数量限制
     * @return 关注列表
     */
    List<UserSubscription> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 获取用户的粉丝列表
     * 
     * @param targetUserId 被关注的用户ID
     * @param offset 偏移量
     * @param limit 数量限制
     * @return 粉丝列表
     */
    List<UserSubscription> findByTargetUserId(@Param("targetUserId") Long targetUserId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 统计用户关注数
     * 
     * @param userId 用户ID
     * @return 关注数
     */
    int countByUserId(Long userId);
    
    /**
     * 统计用户粉丝数
     * 
     * @param targetUserId 被关注的用户ID
     * @return 粉丝数
     */
    int countByTargetUserId(Long targetUserId);
} 