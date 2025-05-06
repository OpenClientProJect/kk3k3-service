package com.ligg.admin.service.impl;

import com.ligg.admin.mapper.AdminCommentMapper;
import com.ligg.admin.service.AdminCommentService;
import com.ligg.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCommentServiceImpl implements AdminCommentService {

    @Autowired
    private AdminCommentMapper adminCommentMapper;

    @Override
    public List<Comment> getAllComments() {
        return adminCommentMapper.getAllComments();
    }

    @Override
    public void deleteComment(Long id) {
        adminCommentMapper.deleteComment(id);
    }

    @Override
    public void statusComment(Long id, Integer status) {
        if (status == 1){
            adminCommentMapper.statusComment(id, 0);
        }else {
            adminCommentMapper.statusComment(id, 1);
        }
    }
}
