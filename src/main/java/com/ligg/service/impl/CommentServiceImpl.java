package com.ligg.service.impl;

import com.ligg.entity.Comment;
import com.ligg.entity.User;
import com.ligg.entity.Video;
import com.ligg.mapper.CommentMapper;
import com.ligg.mapper.UserMapper;
import com.ligg.mapper.VideoMapper;
import com.ligg.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论服务实现类
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private VideoMapper videoMapper;
    
    @Override
    @Transactional
    public boolean addComment(Comment comment) {

        // 设置评论属性
        comment.setParentId(null); // 顶级评论
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        
        return commentMapper.insert(comment) > 0;
    }
    
    @Override
    @Transactional
    public boolean replyComment(Comment comment) {

        // 检查父评论是否存在
        Comment parentComment = commentMapper.findById(comment.getParentId());
        if (parentComment == null || parentComment.getStatus() != 1) {
            log.warn("尝试回复不存在或已删除的评论, parentId={}", comment.getParentId());
            return false;
        }
        
        // 设置评论属性
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        
        return commentMapper.insert(comment) > 0;
    }
    
    @Override
    public Map<String, Object> getVideoComments(Long videoId, Long userId, int page, int size) {
        int offset = (page - 1) * size;
        
        // 查询顶级评论
        List<Comment> topLevelComments = commentMapper.findTopLevelByVideoId(videoId, offset, size);
        
        // 处理评论数据，包括设置用户信息和回复列表
        List<Comment> processedComments = new ArrayList<>();
        for (Comment comment : topLevelComments) {
            // 设置用户信息
            User user = userMapper.findById(comment.getUserId());
            if (user != null) {
                comment.setUsername(user.getUsername());
                comment.setUserAvatar(user.getAvatarUrl());
            }
            

            // 获取回复列表
            List<Comment> replies = commentMapper.findRepliesByParentId(comment.getId());
            if (replies != null && !replies.isEmpty()) {
                // 处理回复的用户信息和点赞状态
                for (Comment reply : replies) {
                    User replyUser = userMapper.findById(reply.getUserId());
                    if (replyUser != null) {
                        reply.setUsername(replyUser.getUsername());
                        reply.setUserAvatar(replyUser.getAvatarUrl());
                    }
                    
                }
                comment.setReplies(replies);
            } else {
                comment.setReplies(new ArrayList<>());
            }
            
            processedComments.add(comment);
        }
        
        // 统计评论总数
        int total = commentMapper.countByVideoId(videoId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("comments", processedComments);
        result.put("total", total);
        return result;
    }
    
    @Override
    @Transactional
    public boolean deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null || comment.getStatus() != 1) {
            log.warn("尝试删除不存在或已删除的评论, commentId={}", commentId);
            return false;
        }
        
        // 逻辑删除评论
        return commentMapper.delete(commentId) > 0;
    }

    //获取评论获取消息
    @Override
    public List<Comment> getReplyMessages(Long id) {
        List<Comment> Comment = commentMapper. getReplyMessages(id);
        return Comment;
    }
}