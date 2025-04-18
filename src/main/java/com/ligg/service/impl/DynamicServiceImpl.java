package com.ligg.service.impl;

import com.ligg.entity.Dynamic;
import com.ligg.entity.DynamicComment;
import com.ligg.entity.DynamicLike;
import com.ligg.entity.User;
import com.ligg.entity.Video;
import com.ligg.mapper.DynamicCommentMapper;
import com.ligg.mapper.DynamicLikeMapper;
import com.ligg.mapper.DynamicMapper;
import com.ligg.mapper.UserMapper;
import com.ligg.mapper.VideoMapper;
import com.ligg.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DynamicServiceImpl implements DynamicService {
    @Autowired
    private DynamicMapper dynamicMapper;
    
    @Autowired
    private DynamicLikeMapper dynamicLikeMapper;
    
    @Autowired
    private DynamicCommentMapper dynamicCommentMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private VideoMapper videoMapper;
    
    @Override
    @Transactional
    public Dynamic publishDynamic(Long userId, String content, String images, Long videoId) {
        Dynamic dynamic = new Dynamic();
        dynamic.setUserId(userId);
        dynamic.setContent(content);
        dynamic.setImages(images);
        dynamic.setVideoId(videoId);
        dynamic.setLikes(0);
        dynamic.setComments(0);
        dynamic.setStatus(1);
        
        dynamicMapper.insert(dynamic);
        
        // 查询用户信息
        User user = userMapper.findById(userId);
        if (user != null) {
            dynamic.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
            dynamic.setUserAvatar(user.getAvatarUrl());
        }
        
        // 如果关联了视频，查询视频信息
        if (videoId != null) {
            Video video = videoMapper.selectById(videoId);
            if (video != null) {
                dynamic.setVideoTitle(video.getTitle());
                dynamic.setVideoCover(video.getCoverUrl());
            }
        }
        
        return dynamic;
    }
    
    @Override
    @Transactional
    public boolean deleteDynamic(Long dynamicId, Long userId) {
        Dynamic dynamic = dynamicMapper.selectById(dynamicId);
        if (dynamic == null || !dynamic.getUserId().equals(userId)) {
            return false;
        }
        
        return dynamicMapper.deleteById(dynamicId) > 0;
    }
    
    @Override
    @Transactional
    public boolean likeDynamic(Long dynamicId, Long userId) {
        DynamicLike like = dynamicLikeMapper.selectByDynamicIdAndUserId(dynamicId, userId);
        
        if (like != null) {
            // 取消点赞
            dynamicLikeMapper.deleteByDynamicIdAndUserId(dynamicId, userId);
            dynamicMapper.updateLikeCount(dynamicId, -1);
            return false;
        } else {
            // 点赞
            like = new DynamicLike();
            like.setDynamicId(dynamicId);
            like.setUserId(userId);
            dynamicLikeMapper.insert(like);
            dynamicMapper.updateLikeCount(dynamicId, 1);
            return true;
        }
    }
    
    @Override
    public boolean isLiked(Long dynamicId, Long userId) {
        if (userId == null) {
            return false;
        }
        return dynamicLikeMapper.selectByDynamicIdAndUserId(dynamicId, userId) != null;
    }
    
    @Override
    public Map<String, Object> getDynamicDetail(Long dynamicId, Long currentUserId) {
        Dynamic dynamic = dynamicMapper.selectById(dynamicId);
        if (dynamic == null) {
            return null;
        }
        
        // 查询用户信息
        User user = userMapper.findById(dynamic.getUserId());
        if (user != null) {
            dynamic.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
            dynamic.setUserAvatar(user.getAvatarUrl());
        }
        
        // 判断当前用户是否点赞
        if (currentUserId != null) {
            dynamic.setLiked(isLiked(dynamicId, currentUserId));
        } else {
            dynamic.setLiked(false);
        }
        
        // 如果关联了视频，查询视频信息
        if (dynamic.getVideoId() != null) {
            Video video = videoMapper.selectById(dynamic.getVideoId());
            if (video != null) {
                dynamic.setVideoTitle(video.getTitle());
                dynamic.setVideoCover(video.getCoverUrl());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("dynamic", dynamic);
        return result;
    }
    
    @Override
    public Map<String, Object> getUserDynamics(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        List<Dynamic> dynamics = dynamicMapper.selectByUserId(userId, offset, size);
        int total = dynamicMapper.countByUserId(userId);
        
        // 查询用户信息
        User user = userMapper.findById(userId);
        
        // 填充动态展示信息
        for (Dynamic dynamic : dynamics) {
            if (user != null) {
                dynamic.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
                dynamic.setUserAvatar(user.getAvatarUrl());
            }
            
            // 如果关联了视频，查询视频信息
            if (dynamic.getVideoId() != null) {
                Video video = videoMapper.selectById(dynamic.getVideoId());
                if (video != null) {
                    dynamic.setVideoTitle(video.getTitle());
                    dynamic.setVideoCover(video.getCoverUrl());
                }
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("dynamics", dynamics);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getLatestDynamics(int page, int size, Long currentUserId) {
        int offset = (page - 1) * size;
        List<Dynamic> dynamics = dynamicMapper.selectLatest(offset, size);
        int total = dynamicMapper.countTotal();
        
        // 查询用户信息和点赞状态
        for (Dynamic dynamic : dynamics) {
            User user = userMapper.findById(dynamic.getUserId());
            if (user != null) {
                dynamic.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
                dynamic.setUserAvatar(user.getAvatarUrl());
            }
            
            // 判断当前用户是否点赞
            if (currentUserId != null) {
                dynamic.setLiked(isLiked(dynamic.getId(), currentUserId));
            } else {
                dynamic.setLiked(false);
            }
            
            // 如果关联了视频，查询视频信息
            if (dynamic.getVideoId() != null) {
                Video video = videoMapper.selectById(dynamic.getVideoId());
                if (video != null) {
                    dynamic.setVideoTitle(video.getTitle());
                    dynamic.setVideoCover(video.getCoverUrl());
                }
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("dynamics", dynamics);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }
    
    @Override
    @Transactional
    public DynamicComment comment(Long dynamicId, Long userId, String content, Long parentId) {
        // 检查动态是否存在
        Dynamic dynamic = dynamicMapper.selectById(dynamicId);
        if (dynamic == null) {
            return null;
        }
        
        // 创建评论
        DynamicComment comment = new DynamicComment();
        comment.setDynamicId(dynamicId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setStatus(1);
        
        dynamicCommentMapper.insert(comment);
        
        // 更新动态评论数
        dynamicMapper.updateCommentCount(dynamicId, 1);
        
        // 填充用户信息
        User user = userMapper.findById(userId);
        if (user != null) {
            comment.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
            comment.setUserAvatar(user.getAvatarUrl());
        }
        
        return comment;
    }
    
    @Override
    public Map<String, Object> getDynamicComments(Long dynamicId, int page, int size) {
        System.out.println("服务层获取动态评论, dynamicId=" + dynamicId + ", page=" + page + ", size=" + size);
        
        int offset = (page - 1) * size;
        List<DynamicComment> comments = dynamicCommentMapper.selectByDynamicId(dynamicId, offset, size);
        int total = dynamicCommentMapper.countByDynamicId(dynamicId);
        
        System.out.println("查询到评论数量: " + (comments != null ? comments.size() : 0) + ", 总评论数: " + total);
        
        // 确保comments不为null
        if (comments == null) {
            comments = new ArrayList<>();
        }
        
        // 查询用户信息和回复信息
        for (DynamicComment comment : comments) {
            User user = userMapper.findById(comment.getUserId());
            if (user != null) {
                comment.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
                comment.setUserAvatar(user.getAvatarUrl());
            }
            
            // 查询回复
            List<DynamicComment> replies = dynamicCommentMapper.selectRepliesByParentId(comment.getId());
            
            // 确保replies不为null
            if (replies == null) {
                replies = new ArrayList<>();
            }
            
            // 填充回复的用户信息
            for (DynamicComment reply : replies) {
                User replyUser = userMapper.findById(reply.getUserId());
                if (replyUser != null) {
                    reply.setUsername(replyUser.getNickname() != null ? replyUser.getNickname() : replyUser.getUsername());
                    reply.setUserAvatar(replyUser.getAvatarUrl());
                }
            }
            
            comment.setReplies(replies);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("comments", comments);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }
    
    @Override
    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        DynamicComment comment = dynamicCommentMapper.selectById(commentId);
        if (comment == null) {
            return false;
        }
        
        // 检查是否有权限删除（评论者或动态发布者）
        Dynamic dynamic = dynamicMapper.selectById(comment.getDynamicId());
        if (!comment.getUserId().equals(userId) && !dynamic.getUserId().equals(userId)) {
            return false;
        }
        
        // 删除评论
        if (dynamicCommentMapper.deleteById(commentId) > 0) {
            // 更新动态评论数
            dynamicMapper.updateCommentCount(comment.getDynamicId(), -1);
            return true;
        }
        
        return false;
    }
} 